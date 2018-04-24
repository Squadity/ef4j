package net.bolbat.ef4j.core.channel.inprocess;

import net.bolbat.ef4j.api.channel.ChannelType;

public final class InProcessChannelType implements ChannelType {

	public static final String ID = "IN_PROCESS";
	public static final InProcessChannelType INSTANCE = new InProcessChannelType();

	private InProcessChannelType() {
	}

	@Override
	public String getId() {
		return ID;
	}

}
