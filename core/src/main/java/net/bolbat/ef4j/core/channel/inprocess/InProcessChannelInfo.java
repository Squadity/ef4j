package net.bolbat.ef4j.core.channel.inprocess;

import static net.bolbat.utils.lang.StringUtils.isEmpty;

import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.channel.ChannelType;

public class InProcessChannelInfo implements ChannelInfo {

	private final String id;

	public InProcessChannelInfo(final String aId) {
		if (isEmpty(aId))
			throw new IllegalArgumentException("aId argument is empty");
		this.id = aId;
	}

	public static InProcessChannelInfo of(final String aId) {
		return new InProcessChannelInfo(aId);
	}

	@Override
	public final ChannelType getType() {
		return InProcessChannelType.INSTANCE;
	}

	@Override
	public String getId() {
		return id;
	}

}
