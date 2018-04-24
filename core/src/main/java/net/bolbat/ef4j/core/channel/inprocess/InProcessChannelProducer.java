package net.bolbat.ef4j.core.channel.inprocess;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.bolbat.ef4j.api.processing.DefaultProcessingErrors;
import net.bolbat.ef4j.api.producer.Producer;
import net.bolbat.ef4j.api.producer.ProducerListener;
import net.bolbat.ef4j.api.producer.ProducerNoopListener;
import net.bolbat.ef4j.api.producer.ProducerOptions;

public class InProcessChannelProducer<E> implements Producer<E> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InProcessChannelProducer.class);

	private final AtomicBoolean started = new AtomicBoolean(true);
	private final InProcessChannelImpl<E> channel;
	private final ProducerListener<E> listener;

	protected InProcessChannelProducer(final InProcessChannelImpl<E> aChannel, final ProducerOptions aOptions) {
		if (aChannel == null)
			throw new IllegalArgumentException("aChannel argument is null");
		this.channel = aChannel;

		if (aOptions == null)
			throw new IllegalArgumentException("aOptions argument is null");

		final ProducerListener<E> aListener = aOptions.getListener();
		this.listener = aListener != null ? aListener : ProducerNoopListener.newListener();
	}

	@Override
	public void produce(final E event) {
		if (!started.get())
			throw new InProcessChannelException("is not started");

		channel.getConsumers().forEach(consumer -> {
			try {
				consumer.consume(event);
				// CHECKSTYLE:OFF
			} catch (final Throwable e) {
				// CHECKSTYLE:ON
				listener.onError(event, DefaultProcessingErrors.CONSUMER_ERROR, e);
				LOGGER.warn("publishing error, skipping", e);
			}
		});
	}

	protected void stop() {
		started.set(false);
	}

}
