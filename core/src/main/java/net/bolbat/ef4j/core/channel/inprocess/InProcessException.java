package net.bolbat.ef4j.core.channel.inprocess;

import net.bolbat.ef4j.api.channel.ChannelException;

public class InProcessException extends ChannelException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -3451170848217748206L;

	/**
	 * Default constructor.
	 */
	public InProcessException() {
		super();
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public InProcessException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public InProcessException(final Throwable cause) {
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
	public InProcessException(final String message, final Throwable cause) {
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
	public InProcessException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
