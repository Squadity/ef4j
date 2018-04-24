package net.bolbat.ef4j.api.channel;

import net.bolbat.ef4j.api.consumer.Consumer;
import net.bolbat.ef4j.api.consumer.ConsumerOptions;
import net.bolbat.ef4j.api.producer.Producer;
import net.bolbat.ef4j.api.producer.ProducerOptions;

public interface Channel<E> {

	ChannelInfo getInfo();

	Producer<E> initProducer();

	Producer<E> initProducer(ProducerOptions options);

	void register(Consumer<E> consumer);

	void register(Consumer<E> consumer, ConsumerOptions options);

	void shutdown(Producer<E> producer);

	void shutdown(Consumer<E> consumer);

	void shutdown();

}
