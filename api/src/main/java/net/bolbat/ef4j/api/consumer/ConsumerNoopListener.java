package net.bolbat.ef4j.api.consumer;

import net.bolbat.ef4j.api.processing.ProcessingError;

public final class ConsumerNoopListener<E> implements ConsumerListener<E> {

	private ConsumerNoopListener() {
	}

	@Override
	public void onError(final E event, final ProcessingError type) {
	}

	@Override
	public void onError(final E event, final ProcessingError type, final Throwable e) {
	}

	public static <T> ConsumerNoopListener<T> newListener() {
		return new ConsumerNoopListener<>();
	}

}
