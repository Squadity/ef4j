package net.bolbat.ef4j.api.consumer;

@FunctionalInterface
public interface Consumer<E> {

	void consume(E event);

}
