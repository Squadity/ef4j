package net.bolbat.ef4j.core.channel;

import net.bolbat.ef4j.api.channel.Channel;
import net.bolbat.ef4j.api.channel.ChannelFactory;
import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.channel.ChannelOptions;
import net.bolbat.ef4j.api.channel.ChannelType;

public interface ChannelService {

	<C> Channel<C> get(ChannelInfo info);

	<C> Channel<C> resolve(ChannelInfo info);

	<C> Channel<C> resolve(ChannelInfo info, ChannelOptions options);

	<C> Channel<C> resolve(ChannelInfo info, ChannelFactory factory);

	<C> Channel<C> resolve(ChannelInfo info, ChannelFactory factory, ChannelOptions options);

	void register(ChannelType type, ChannelFactory factory);

	void unregister(ChannelType type);

	void shutdown(ChannelInfo info);

	void shutdown();

}
