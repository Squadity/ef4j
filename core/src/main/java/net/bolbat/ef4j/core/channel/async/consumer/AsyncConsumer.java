package net.bolbat.ef4j.core.channel.async.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.consumer.Consumer;
import net.bolbat.ef4j.api.consumer.ConsumerListener;
import net.bolbat.ef4j.api.consumer.ConsumerOptions;
import net.bolbat.ef4j.core.channel.async.AsyncProcessingErrors;
import net.bolbat.ef4j.core.channel.async.AsyncUtils;
import net.bolbat.utils.concurrency.ThreadFactoryBuilder;

public class AsyncConsumer<E> implements Consumer<E> {

	private static final String EXECUTOR_THREADS_NAME_FORMAT = "AsyncConsumer[%s|%s]-processor-%d";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final AtomicBoolean started = new AtomicBoolean(false);

	private final ChannelInfo info;
	private final Consumer<E> origin;
	private final AsyncConsumerOptions options;
	private final ConsumerListener<E> listener;
	private final ThreadFactoryBuilder threadFactoryBuilder;

	private BlockingQueue<E> queue;
	private ExecutorService executor;
	private List<AsyncConsumerProcessor<E>> processors;

	public AsyncConsumer(final ChannelInfo aInfo, final Consumer<E> aOrigin, final ConsumerOptions aOptions) {
		if (aInfo == null)
			throw new IllegalArgumentException("aInfo argument is null");
		this.info = aInfo;

		if (aOrigin == null)
			throw new IllegalArgumentException("aOrigin argument is null");
		this.origin = aOrigin;

		if (aOptions == null)
			throw new IllegalArgumentException("aOptions argument is null");

		final AsyncConsumerOptions asyncOptions = aOptions instanceof AsyncConsumerOptions //
				? (AsyncConsumerOptions) aOptions //
				: DefaultAsyncConsumerOptions.INSTANCE;
		this.options = asyncOptions;

		final ConsumerListener<E> aListener = asyncOptions.getListener();
		this.listener = aListener != null ? aListener : DefaultAsyncConsumerOptions.INSTANCE.getListener();

		this.threadFactoryBuilder = new ThreadFactoryBuilder() //
				.setNameFormat(EXECUTOR_THREADS_NAME_FORMAT) //
				.setNameFormatArgs(info.getType().getId(), info.getId()) //
				.setPriority(Thread.NORM_PRIORITY) //
				.setDaemon(false);

		init();
	}

	private void init() {
		final BlockingQueue<E> newQueue = new ArrayBlockingQueue<>(options.getQueueSize());
		final ExecutorService newExecutor = options.getProcessingThreads() == 1 //
				? Executors.newSingleThreadExecutor(threadFactoryBuilder.build()) //
				: Executors.newFixedThreadPool(options.getProcessingThreads(), threadFactoryBuilder.build());
		final List<AsyncConsumerProcessor<E>> newProcessors = new ArrayList<>(options.getProcessingThreads());

		for (int i = 0; i < options.getProcessingThreads(); i++) {
			final AsyncConsumerProcessor<E> unorderedProcessor = new AsyncConsumerProcessor<>(options, listener, newQueue, getOrigin(), logger);
			newExecutor.submit(unorderedProcessor);
			newProcessors.add(unorderedProcessor);
		}

		queue = newQueue;
		processors = newProcessors;
		executor = newExecutor;

		started.set(true);
	}

	public void shutdown() {
		if (!started.compareAndSet(true, false))
			throw new AsyncConsumerException("is not started");

		started.set(false);

		processors.forEach(p -> p.stop());
		processors = null;

		AsyncUtils.shutdown(executor, true, options.getStopTimeout(), options.getStopTimeUnit());
		executor = null;

		queue = null;
	}

	@Override
	public void consume(final E event) {
		if (!started.get())
			throw new AsyncConsumerException("is not started");

		try {
			final boolean offered = queue.offer(event, options.getQueueOfferTimeout(), options.getQueueOfferTimeUnit());
			if (offered)
				return;

			listener.onError(event, AsyncProcessingErrors.ASYNC_QUEUE_OVERFLOW);
			logger.warn(String.format("receive error, queue[size:%s] overflow, skipping", options.getQueueSize()));
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public Consumer<E> getOrigin() {
		return origin;
	}

}
