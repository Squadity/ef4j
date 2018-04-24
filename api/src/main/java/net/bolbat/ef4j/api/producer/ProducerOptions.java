package net.bolbat.ef4j.api.producer;

import net.bolbat.ef4j.api.processing.ProcessingMode;

public interface ProducerOptions {

	ProcessingMode getProcessingMode();

	<E> ProducerListener<E> getListener();

}
