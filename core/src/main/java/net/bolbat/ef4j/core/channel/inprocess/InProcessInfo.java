package net.bolbat.ef4j.core.channel.inprocess;

import static net.bolbat.utils.lang.StringUtils.isEmpty;

import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.channel.ChannelType;

public final class InProcessInfo implements ChannelInfo {

	private final String id;

	public InProcessInfo(final String aId) {
		if (isEmpty(aId))
			throw new IllegalArgumentException("aId argument is empty");
		this.id = aId;
	}

	public static InProcessInfo of(final String aId) {
		return new InProcessInfo(aId);
	}

	@Override
	public ChannelType getType() {
		return InProcessType.INSTANCE;
	}

	@Override
	public String getId() {
		return id;
	}

}
