package net.bolbat.ef4j.api.producer;

@FunctionalInterface
public interface Producer<E> {

	void produce(E event);

}
