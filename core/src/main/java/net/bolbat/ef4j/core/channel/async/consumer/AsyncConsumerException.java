package net.bolbat.ef4j.core.channel.async.consumer;

import net.bolbat.ef4j.api.consumer.ConsumerException;

public class AsyncConsumerException extends ConsumerException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -7172555994479328295L;

	/**
	 * Default constructor.
	 */
	public AsyncConsumerException() {
		super();
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public AsyncConsumerException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public AsyncConsumerException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 * @param cause
	 *            exception cause
	 */
	public AsyncConsumerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 * @param cause
	 *            exception cause
	 * @param enableSuppression
	 *            whether or not suppression is enabled or disabled
	 * @param writableStackTrace
	 *            whether or not the stack trace should be writable
	 */
	public AsyncConsumerException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
