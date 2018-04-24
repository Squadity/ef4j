package net.bolbat.ef4j.core.channel.async.consumer;

import java.util.concurrent.TimeUnit;

public final class DefaultAsyncConsumerOptions implements AsyncConsumerOptions {

	public static final DefaultAsyncConsumerOptions INSTANCE = new DefaultAsyncConsumerOptions();

	public static final int QUEUE_SIZE = 10_000;
	public static final long QUEUE_POLL_TIMEOUT = 50L;
	public static final TimeUnit QUEUE_POLL_TIME_UNIT = TimeUnit.MILLISECONDS;
	public static final long QUEUE_OFFER_TIMEOUT = 10L;
	public static final TimeUnit QUEUE_OFFER_TIME_UNIT = TimeUnit.MILLISECONDS;
	public static final int PROCESSING_THREADS = 1;
	public static final long SHUTDOWN_TIMEOUT = 30L;
	public static final TimeUnit SHUTDOWN_TIME_UNIT = TimeUnit.SECONDS;

	private DefaultAsyncConsumerOptions() {
	}

	@Override
	public int getQueueSize() {
		return QUEUE_SIZE;
	}

	@Override
	public long getQueuePollTimeout() {
		return QUEUE_POLL_TIMEOUT;
	}

	@Override
	public TimeUnit getQueuePollTimeUnit() {
		return QUEUE_POLL_TIME_UNIT;
	}

	@Override
	public long getQueueOfferTimeout() {
		return QUEUE_OFFER_TIMEOUT;
	}

	@Override
	public TimeUnit getQueueOfferTimeUnit() {
		return QUEUE_OFFER_TIME_UNIT;
	}

	@Override
	public int getProcessingThreads() {
		return PROCESSING_THREADS;
	}

	@Override
	public long getShutdownTimeout() {
		return SHUTDOWN_TIMEOUT;
	}

	@Override
	public TimeUnit getShutdownTimeUnit() {
		return SHUTDOWN_TIME_UNIT;
	}

}
