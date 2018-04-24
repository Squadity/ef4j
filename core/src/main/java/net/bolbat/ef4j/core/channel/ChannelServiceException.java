package net.bolbat.ef4j.core.channel;

import net.bolbat.ef4j.api.EF4JException;

public class ChannelServiceException extends EF4JException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 4300086618191105470L;

	/**
	 * Default constructor.
	 */
	public ChannelServiceException() {
		super();
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public ChannelServiceException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public ChannelServiceException(final Throwable cause) {
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
	public ChannelServiceException(final String message, final Throwable cause) {
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
	public ChannelServiceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
