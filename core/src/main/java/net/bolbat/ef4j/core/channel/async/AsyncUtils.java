package net.bolbat.ef4j.core.channel.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AsyncUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUtils.class);

	/**
	 * Default constructor with preventing instantiations of this class.
	 */
	private AsyncUtils() {
		throw new IllegalAccessError("Shouldn't be instantiated.");
	}

	/**
	 * Shutdown {@link ExecutorService} using {@code ExecutorService.shutdown()}.
	 * 
	 * @param service
	 *            service
	 * @param wait
	 *            is need to wait
	 * @param timeout
	 *            waiting timeout
	 * @param unit
	 *            waiting timeout unit
	 */
	public static void shutdown(final ExecutorService service, final boolean wait, final long timeout, final TimeUnit unit) {
		shutdown(service, false, wait, timeout, unit);
	}

	/**
	 * Shutdown {@link ExecutorService}.<br>
	 * {@code ExecutorService.shutdownNow()} will be used if:<br>
	 * - {@code terminate} is <code>false</code>;<br>
	 * - {@code wait} is <code>true</code>;<br>
	 * - {@code timeout} is reached.
	 * 
	 * @param service
	 *            service
	 * @param terminate
	 *            {@code ExecutorService.shutdownNow()} will be used instead of {@code ExecutorService.shutdown()} and waiting will be skipped
	 * @param wait
	 *            is need to wait
	 * @param timeout
	 *            waiting timeout
	 * @param unit
	 *            waiting timeout unit
	 */
	public static void shutdown(final ExecutorService service, final boolean terminate, final boolean wait, final long timeout, final TimeUnit unit) {
		if (service == null)
			throw new IllegalArgumentException("service argument is null");
		if (unit == null)
			throw new IllegalArgumentException("unit argument is null");

		if (terminate) {
			service.shutdownNow();
			return;
		}

		service.shutdown();
		if (service.isTerminated() || !wait)
			return;

		try {
			service.awaitTermination(timeout, unit);
		} catch (final InterruptedException e) {
			LOGGER.warn(String.format("Service[%s] awaitTermination(%s,%s) is interrupted", service, timeout, unit), e);
		} finally {
			if (!service.isTerminated())
				service.shutdownNow();
		}
	}

}
