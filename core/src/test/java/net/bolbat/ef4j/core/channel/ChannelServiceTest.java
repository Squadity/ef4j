package net.bolbat.ef4j.core.channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.bolbat.ef4j.api.channel.Channel;
import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.producer.Producer;
import net.bolbat.ef4j.core.channel.async.consumer.DefaultAsyncConsumerOptions;
import net.bolbat.ef4j.core.channel.inprocess.InProcessChannelInfo;

public class ChannelServiceTest {

	private final String event = "test";
	private final AtomicInteger eventsCount = new AtomicInteger();
	private final ChannelInfo info = InProcessChannelInfo.of("test-channel");
	private ChannelService service;
	private Channel<String> channel;
	private Producer<String> producer;

	@BeforeEach
	public void init() {
		service = ChannelServiceFactory.INSTANCE.createAndConfigure();
		channel = service.resolve(info);
		producer = channel.initProducer();
	}

	@AfterEach
	public void tearDown() {
		eventsCount.set(0);
		service.shutdown();
	}

	@Test
	public void inProcessSyncProducing() {
		channel.register((e) -> {
			Assertions.assertSame(event, e);
			eventsCount.incrementAndGet();
		});

		producer.produce(event);
		Assertions.assertEquals(1, eventsCount.get());
	}

	@Test
	public void inProcessAsyncProducing() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);

		channel.register((e) -> {
			Assertions.assertSame(event, e);
			eventsCount.incrementAndGet();
			latch.countDown();
		}, DefaultAsyncConsumerOptions.INSTANCE);

		producer.produce(event);
		latch.await();
		Assertions.assertEquals(1, eventsCount.get());
	}

}
