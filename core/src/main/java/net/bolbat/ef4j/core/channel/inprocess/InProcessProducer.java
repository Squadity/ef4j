package net.bolbat.ef4j.core.channel.inprocess;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.bolbat.ef4j.api.processing.DefaultProcessingErrors;
import net.bolbat.ef4j.api.producer.Producer;
import net.bolbat.ef4j.api.producer.ProducerListener;
import net.bolbat.ef4j.api.producer.ProducerNoopListener;
import net.bolbat.ef4j.api.producer.ProducerOptions;

public class InProcessProducer<E> implements Producer<E> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InProcessProducer.class);

	private final AtomicBoolean started = new AtomicBoolean(true);
	private final InProcessImpl<E> channel;
	private final ProducerListener<E> listener;

	protected InProcessProducer(final InProcessImpl<E> aChannel, final ProducerOptions aOptions) {
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
			throw new InProcessException("is not started");

		channel.getConsumers().forEach(consumer -> {
			try {
				consumer.consume(event);
			} catch (final Throwable e) {
				listener.onError(event, DefaultProcessingErrors.CONSUMER_ERROR, e);
				LOGGER.warn("publishing error, skipping", e);
			}
		});
	}

	protected void stop() {
		started.set(false);
	}

}
