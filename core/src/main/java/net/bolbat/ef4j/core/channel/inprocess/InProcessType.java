package net.bolbat.ef4j.core.channel.inprocess;

import net.bolbat.ef4j.api.channel.ChannelType;

public final class InProcessType implements ChannelType {

	public static final String ID = "IN_PROCESS";
	public static final InProcessType INSTANCE = new InProcessType();

	private InProcessType() {
	}

	@Override
	public String getId() {
		return ID;
	}

}
