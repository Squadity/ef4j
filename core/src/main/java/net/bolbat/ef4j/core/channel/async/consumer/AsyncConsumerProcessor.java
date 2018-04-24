package net.bolbat.ef4j.core.channel.async.consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import net.bolbat.ef4j.api.consumer.Consumer;
import net.bolbat.ef4j.api.consumer.ConsumerListener;
import net.bolbat.ef4j.api.processing.DefaultProcessingErrors;

public class AsyncConsumerProcessor<E> implements Runnable {

	private final AtomicBoolean started = new AtomicBoolean(true);
	private final AtomicBoolean interrupted = new AtomicBoolean(false);

	private final AsyncConsumerOptions options;
	private final ConsumerListener<E> listener;
	private final BlockingQueue<E> queue;
	private final Consumer<E> origin;
	private final Logger logger;

	public AsyncConsumerProcessor(final AsyncConsumerOptions aOptions, final ConsumerListener<E> aListener, //
			final BlockingQueue<E> aQueue, final Consumer<E> aOrigin, final Logger aLogger) {
		if (aOptions == null)
			throw new IllegalArgumentException("aOptions argument is null");
		this.options = aOptions;

		if (aListener == null)
			throw new IllegalArgumentException("aListener argument is null");
		this.listener = aListener;

		if (aQueue == null)
			throw new IllegalArgumentException("aQueue argument is null");
		this.queue = aQueue;

		if (aOrigin == null)
			throw new IllegalArgumentException("aOrigin argument is null");
		this.origin = aOrigin;

		if (aLogger == null)
			throw new IllegalArgumentException("aLogger argument is null");
		this.logger = aLogger;
	}

	@Override
	public void run() {
		while (started.get() || !queue.isEmpty()) {
			process();
		}

		if (interrupted.get())
			Thread.currentThread().interrupt();
	}

	protected void process() {
		E polled = null;
		try {
			polled = queue.poll(options.getQueuePollTimeout(), options.getQueuePollTimeUnit());
		} catch (final InterruptedException e) {
			interrupted.set(true);
		}

		if (polled == null)
			return;

		try {
			origin.consume(polled);
			// CHECKSTYLE:OFF
		} catch (final Throwable e) {
			// CHECKSTYLE:ON
			listener.onError(polled, DefaultProcessingErrors.CONSUMER_ERROR, e);
			logger.warn("processing error, skipping", e);
		}
	}

	public void stop() {
		started.set(false);
	}

}
