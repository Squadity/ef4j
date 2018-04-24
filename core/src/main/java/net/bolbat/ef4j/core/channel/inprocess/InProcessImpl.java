package net.bolbat.ef4j.core.channel.inprocess;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.bolbat.ef4j.api.channel.Channel;
import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.channel.ChannelOptions;
import net.bolbat.ef4j.api.consumer.Consumer;
import net.bolbat.ef4j.api.consumer.ConsumerOptions;
import net.bolbat.ef4j.api.consumer.DefaultConsumerOptions;
import net.bolbat.ef4j.api.producer.DefaultProducerOptions;
import net.bolbat.ef4j.api.producer.Producer;
import net.bolbat.ef4j.api.producer.ProducerOptions;
import net.bolbat.ef4j.core.channel.async.consumer.AsyncConsumer;

public class InProcessImpl<E> implements Channel<E> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InProcessImpl.class);

	private final ChannelInfo info;
	private final List<Producer<E>> producers = new CopyOnWriteArrayList<>();
	private final List<Consumer<E>> consumers = new CopyOnWriteArrayList<>();

	public InProcessImpl(final ChannelInfo aInfo, final ChannelOptions aOptions) {
		if (aInfo == null)
			throw new IllegalArgumentException("aInfo argument is null");
		this.info = aInfo;

		if (aOptions == null)
			throw new IllegalArgumentException("aOptions argument is null");
	}

	@Override
	public ChannelInfo getInfo() {
		return info;
	}

	@Override
	public Producer<E> initProducer() {
		return initProducer(DefaultProducerOptions.INSTANCE);
	}

	@Override
	public Producer<E> initProducer(final ProducerOptions options) {
		if (options == null)
			throw new IllegalArgumentException("options argument is null");

		synchronized (producers) {
			final Producer<E> newProducer = createProducer(options);
			getProducers().add(newProducer);
			return newProducer;
		}
	}

	@Override
	public void register(final Consumer<E> consumer) {
		register(consumer, DefaultConsumerOptions.INSTANCE);
	}

	@Override
	public void register(final Consumer<E> consumer, final ConsumerOptions options) {
		if (consumer == null)
			throw new IllegalArgumentException("consumer argument is null");
		if (options == null)
			throw new IllegalArgumentException("options argument is null");

		synchronized (consumers) {
			if (getConsumer(consumer) != null)
				throw new InProcessException(String.format("Consumer[%s] already exist", consumer));

			switch (options.getProcessingMode()) {
				case SYNC:
					getConsumers().add(consumer);
					break;
				case ASYNC:
					final AsyncConsumer<E> asyncConsumer = new AsyncConsumer<>(info, consumer, options);
					getConsumers().add(asyncConsumer);
					break;
				default:
					throw new InProcessException(String.format("Operating mode[%s] is unsupported", options.getProcessingMode()));
			}
		}
	}

	public void shutdown(final Producer<E> producer) {
		if (producer == null)
			throw new IllegalArgumentException("producer argument is null");

		synchronized (producers) {
			final Producer<E> resolved = getProducer(producer);
			if (resolved == null)
				throw new InProcessException(String.format("Producer[%s] is not exist", producer));

			getProducers().remove(resolved);

			((InProcessProducer<?>) resolved).stop(); // TODO review this

			// TODO Implement 'Async' support
		}
	}

	@Override
	public void shutdown(final Consumer<E> consumer) {
		if (consumer == null)
			throw new IllegalArgumentException("consumer argument is null");

		synchronized (consumers) {
			final Consumer<E> resolved = getConsumer(consumer);
			if (resolved == null)
				throw new InProcessException(String.format("Consumer[%s] is not exist", consumer));

			getConsumers().remove(resolved);

			if (resolved instanceof AsyncConsumer) {
				final AsyncConsumer<?> asyncConsumer = (AsyncConsumer<?>) resolved;
				asyncConsumer.shutdown();
			}
		}
	}

	@Override
	public void shutdown() {
		// producers
		for (final Producer<E> producer : producers)
			try {
				shutdown(producer);
				// CHECKSTYLE:OFF
			} catch (final Throwable e) {
				// CHECKSTYLE:ON
				LOGGER.warn(String.format("Channel[%s|%s] producer[%s] shutdown failed, skipping", info.getId(), info.getType().getId(), producer), e);
			} finally {
				producers.remove(producer);
			}

		// consumers
		for (final Consumer<E> consumer : consumers) {
			try {
				shutdown(consumer);
				// CHECKSTYLE:OFF
			} catch (final Throwable e) {
				// CHECKSTYLE:ON
				LOGGER.warn(String.format("Channel[%s|%s] consumer[%s] shutdown failed, skipping", info.getId(), info.getType().getId(), consumer), e);
			} finally {
				consumers.remove(consumer);
			}
		}
	}

	protected List<Producer<E>> getProducers() {
		return producers;
	}

	protected List<Consumer<E>> getConsumers() {
		return consumers;
	}

	protected Producer<E> createProducer(final ProducerOptions options) {
		// TODO Implement 'Async' support

		return new InProcessProducer<>(this, options);
	}

	protected Producer<E> getProducer(final Producer<E> origin) {
		for (final Producer<E> existing : getProducers()) {
			if (existing == origin)
				return existing;

			// TODO Implement 'Async' support
		}

		return null;
	}

	protected Consumer<E> getConsumer(final Consumer<E> origin) {
		for (final Consumer<E> existing : getConsumers()) {
			if (existing == origin)
				return existing;

			if (existing instanceof AsyncConsumer) {
				final AsyncConsumer<E> asyncExisting = (AsyncConsumer<E>) existing;
				if (asyncExisting.getOrigin() == origin)
					return existing;
			}
		}

		return null;
	}

}
