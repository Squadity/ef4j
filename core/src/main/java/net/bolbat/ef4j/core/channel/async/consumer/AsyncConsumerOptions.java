package net.bolbat.ef4j.core.channel.async.consumer;

import java.util.concurrent.TimeUnit;

import net.bolbat.ef4j.api.consumer.ConsumerListener;
import net.bolbat.ef4j.api.consumer.ConsumerNoopListener;
import net.bolbat.ef4j.api.consumer.ConsumerOptions;
import net.bolbat.ef4j.api.processing.ProcessingMode;

public interface AsyncConsumerOptions extends ConsumerOptions {

	@Override
	default ProcessingMode getProcessingMode() {
		return ProcessingMode.ASYNC;
	}

	@Override
	default <E> ConsumerListener<E> getListener() {
		return ConsumerNoopListener.newListener();
	}

	int getQueueSize();

	long getQueuePollTimeout();

	TimeUnit getQueuePollTimeUnit();

	long getQueueOfferTimeout();

	TimeUnit getQueueOfferTimeUnit();

	int getProcessingThreads();

	long getShutdownTimeout();

	TimeUnit getShutdownTimeUnit();

}
