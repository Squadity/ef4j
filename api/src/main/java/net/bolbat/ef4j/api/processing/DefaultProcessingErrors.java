package net.bolbat.ef4j.api.processing;

public class DefaultProcessingErrors {

	public static final ProcessingError CHANNEL_ERROR = () -> "CHANNEL_ERROR";
	public static final ProcessingError PRODUCER_ERROR = () -> "PRODUCER_ERROR";
	public static final ProcessingError CONSUMER_ERROR = () -> "CONSUMER_ERROR";

}
