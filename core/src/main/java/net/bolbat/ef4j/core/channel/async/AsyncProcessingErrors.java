package net.bolbat.ef4j.core.channel.async;

import net.bolbat.ef4j.api.processing.ProcessingError;

public class AsyncProcessingErrors {

	public static final ProcessingError ASYNC_QUEUE_OVERFLOW = () -> "ASYNC_QUEUE_OVERFLOW";

}
