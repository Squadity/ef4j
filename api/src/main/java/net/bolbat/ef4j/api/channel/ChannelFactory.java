package net.bolbat.ef4j.api.channel;

@FunctionalInterface
public interface ChannelFactory {

	<T> Channel<T> create(ChannelInfo info, ChannelOptions options);

}
