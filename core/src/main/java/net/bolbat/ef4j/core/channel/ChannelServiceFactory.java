package net.bolbat.ef4j.core.channel;

import net.bolbat.ef4j.core.channel.inprocess.InProcessChannelFactory;
import net.bolbat.ef4j.core.channel.inprocess.InProcessChannelType;

public final class ChannelServiceFactory {

	public static final ChannelServiceFactory INSTANCE = new ChannelServiceFactory();

	public ChannelService create() {
		return new ChannelServiceImpl();
	}

	public ChannelService createAndConfigure() {
		final ChannelService result = create();

		// configuring default types
		result.register(InProcessChannelType.INSTANCE, new InProcessChannelFactory());

		return result;
	}

}
