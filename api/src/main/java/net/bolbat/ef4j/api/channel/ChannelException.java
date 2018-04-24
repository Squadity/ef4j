package net.bolbat.ef4j.api.channel;

import net.bolbat.ef4j.api.EF4JException;

public class ChannelException extends EF4JException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -7204729144154337641L;

	/**
	 * Default constructor.
	 */
	public ChannelException() {
		super();
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public ChannelException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public ChannelException(final Throwable cause) {
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
	public ChannelException(final String message, final Throwable cause) {
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
	public ChannelException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
