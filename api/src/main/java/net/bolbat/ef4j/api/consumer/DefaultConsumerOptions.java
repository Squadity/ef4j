package net.bolbat.ef4j.api.consumer;

import net.bolbat.ef4j.api.processing.ProcessingMode;

public final class DefaultConsumerOptions implements ConsumerOptions {

	public static final DefaultConsumerOptions INSTANCE = new DefaultConsumerOptions();

	private DefaultConsumerOptions() {
	}

	@Override
	public ProcessingMode getProcessingMode() {
		return ProcessingMode.DEFAULT;

	}

	@Override
	public <E> ConsumerListener<E> getListener() {
		return ConsumerNoopListener.newListener();
	}

}
