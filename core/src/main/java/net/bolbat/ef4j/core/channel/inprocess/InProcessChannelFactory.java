package net.bolbat.ef4j.core.channel.inprocess;

import net.bolbat.ef4j.api.channel.Channel;
import net.bolbat.ef4j.api.channel.ChannelFactory;
import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.channel.ChannelOptions;

public final class InProcessChannelFactory implements ChannelFactory {

	public static final InProcessChannelFactory INSTANCE = new InProcessChannelFactory();

	@Override
	public <T> Channel<T> create(final ChannelInfo info, final ChannelOptions options) {
		return new InProcessChannelImpl<>(info, options);
	}

}
