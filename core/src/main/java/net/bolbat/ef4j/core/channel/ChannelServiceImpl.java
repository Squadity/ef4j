package net.bolbat.ef4j.core.channel;

import static net.bolbat.utils.lang.StringUtils.isEmpty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.bolbat.ef4j.api.channel.Channel;
import net.bolbat.ef4j.api.channel.ChannelFactory;
import net.bolbat.ef4j.api.channel.ChannelInfo;
import net.bolbat.ef4j.api.channel.ChannelOptions;
import net.bolbat.ef4j.api.channel.ChannelType;
import net.bolbat.ef4j.api.channel.DefaultChannelOptions;
import net.bolbat.utils.concurrency.lock.ConcurrentIdBasedLockManager;
import net.bolbat.utils.concurrency.lock.IdBasedLock;
import net.bolbat.utils.concurrency.lock.IdBasedLockManager;
import net.bolbat.utils.lang.CastUtils;

public class ChannelServiceImpl implements ChannelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelServiceImpl.class);

	private final ConcurrentMap<ChannelKey, Channel<?>> channels = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, ChannelFactory> factories = new ConcurrentHashMap<>();
	private final IdBasedLockManager<ChannelKey> locksManager = new ConcurrentIdBasedLockManager<>();

	@Override
	public <C> Channel<C> get(final ChannelInfo info) {
		validate(info);

		final ChannelKey key = toKey(info);
		final Channel<C> channel = CastUtils.cast(channels.get(key));
		if (channel == null)
			throw new ChannelServiceException(String.format("Channel[%s|%s] is not exist", info.getId(), info.getType().getId()));

		return channel;
	}

	@Override
	public <C> Channel<C> resolve(final ChannelInfo info) {
		validate(info);

		final ChannelFactory factory = resolve(info.getType());
		return resolve(info, factory);
	}

	@Override
	public <C> Channel<C> resolve(final ChannelInfo info, final ChannelOptions options) {
		validate(info);
		validate(options);

		final ChannelFactory factory = resolve(info.getType());
		return resolve(info, factory, options);
	}

	@Override
	public <C> Channel<C> resolve(final ChannelInfo info, final ChannelFactory factory) {
		return resolve(info, factory, DefaultChannelOptions.INSTANCE);
	}

	@Override
	public <C> Channel<C> resolve(final ChannelInfo info, final ChannelFactory factory, final ChannelOptions options) {
		validate(info);
		validate(factory);
		validate(options);

		final ChannelKey key = toKey(info);
		try (final IdBasedLock<ChannelKey> lock = locksManager.obtainLock(key).lock()) {
			final Channel<?> existing = channels.get(key);
			if (existing != null)
				return CastUtils.cast(existing);

			final Channel<Object> channel = factory.create(info, options);
			if (channel == null)
				throw new ChannelServiceException(String.format("Created channel[%s|%s] is null", info.getId(), info.getType().getId()));

			channels.put(key, channel);
			return CastUtils.cast(channel);
		}
	}

	@Override
	public void register(final ChannelType type, final ChannelFactory factory) {
		validate(type);
		validate(factory);

		synchronized (factories) {
			final ChannelFactory existing = factories.get(type.getId());
			if (existing != null)
				throw new ChannelServiceException(String.format("ChannelType[%s] factory[%s] already registered", type.getId(), existing.getClass()));

			factories.put(type.getId(), factory);
		}
	}

	@Override
	public void unregister(final ChannelType type) {
		validate(type);

		synchronized (factories) {
			final ChannelFactory removed = factories.remove(type.getId());
			if (removed == null)
				throw new ChannelServiceException(String.format("ChannelType[%s] factory is not registered", type.getId()));
		}
	}

	@Override
	public void shutdown(final ChannelInfo info) {
		validate(info);

		final ChannelKey key = toKey(info);
		shutdown(key);
	}

	@PreDestroy
	public void shutdown() {
		for (final ChannelKey key : channels.keySet()) {
			try {
				shutdown(key);
			} catch (final Throwable e) {
				LOGGER.warn(String.format("Channel[%s|%s] shutdown failed, skipping", key.getId(), key.getTypeId()), e);
			} finally {
				channels.remove(key);
			}
		}
	}

	private void shutdown(final ChannelKey key) {
		try (final IdBasedLock<ChannelKey> lock = locksManager.obtainLock(key).lock()) {
			final Channel<?> channel = channels.remove(key);
			if (channel == null)
				throw new ChannelServiceException(String.format("Channel[%s|%s] is not exist", key.getId(), key.getTypeId()));

			channel.shutdown();
		}
	}

	private ChannelKey toKey(final ChannelInfo info) {
		return new ChannelKey(info);
	}

	private ChannelFactory resolve(final ChannelType type) {
		final ChannelFactory factory = factories.get(type.getId());
		if (factory != null)
			return factory;

		throw new ChannelServiceException(String.format("ChannelType[%s] factory is not registered", type.getId()));
	}

	private void validate(final ChannelInfo info) {
		if (info == null)
			throw new IllegalArgumentException("info argument is null");
		if (isEmpty(info.getId()))
			throw new IllegalArgumentException("info.id argument is empty");

		validate(info.getType());
	}

	private void validate(final ChannelType type) {
		if (type == null)
			throw new IllegalArgumentException("type argument is null");
		if (isEmpty(type.getId()))
			throw new IllegalArgumentException("type.id argument is empty");
	}

	private void validate(final ChannelFactory factory) {
		if (factory == null)
			throw new IllegalArgumentException("factory argument is null");
	}

	private void validate(final ChannelOptions options) {
		if (options == null)
			throw new IllegalArgumentException("options argument is null");
	}

	private static class ChannelKey {

		private final String id;
		private final String typeId;

		public ChannelKey(final ChannelInfo info) {
			this.id = info.getId();
			this.typeId = info.getType().getId();
		}

		public String getId() {
			return id;
		}

		public String getTypeId() {
			return typeId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final ChannelKey other = (ChannelKey) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (typeId == null) {
				if (other.typeId != null)
					return false;
			} else if (!typeId.equals(other.typeId))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return new StringBuilder("ChannelKey[").append(id).append("|").append(typeId).append("]").toString();
		}

	}

}
