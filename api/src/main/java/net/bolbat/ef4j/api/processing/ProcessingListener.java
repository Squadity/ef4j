package net.bolbat.ef4j.api.processing;

public interface ProcessingListener<E> {

	void onError(E event, ProcessingError type);

	void onError(E event, ProcessingError type, Throwable e);

}
