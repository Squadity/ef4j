package net.bolbat.ef4j.api.producer;

import net.bolbat.ef4j.api.processing.ProcessingError;

public class ProducerNoopListener<E> implements ProducerListener<E> {

	private ProducerNoopListener() {
	}

	@Override
	public void onError(final E event, final ProcessingError type) {
	}

	@Override
	public void onError(final E event, final ProcessingError type, final Throwable e) {
	}

	public static <T> ProducerNoopListener<T> newListener() {
		return new ProducerNoopListener<>();
	}

}
