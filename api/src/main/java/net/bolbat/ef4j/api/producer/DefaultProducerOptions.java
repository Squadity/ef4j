package net.bolbat.ef4j.api.producer;

import net.bolbat.ef4j.api.processing.ProcessingMode;

public final class DefaultProducerOptions implements ProducerOptions {

	public static final DefaultProducerOptions INSTANCE = new DefaultProducerOptions();

	private DefaultProducerOptions() {
	}

	@Override
	public ProcessingMode getProcessingMode() {
		return ProcessingMode.DEFAULT;
	}

	@Override
	public <E> ProducerListener<E> getListener() {
		return ProducerNoopListener.newListener();
	}

}
