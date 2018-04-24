package net.bolbat.ef4j.api.consumer;

import net.bolbat.ef4j.api.processing.ProcessingMode;

public interface ConsumerOptions {

	ProcessingMode getProcessingMode();

	<E> ConsumerListener<E> getListener();

}
