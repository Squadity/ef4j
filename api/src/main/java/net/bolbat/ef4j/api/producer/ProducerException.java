package net.bolbat.ef4j.api.producer;

import net.bolbat.ef4j.api.EF4JException;

public class ProducerException extends EF4JException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 3308079277606255463L;

	/**
	 * Default constructor.
	 */
	public ProducerException() {
		super();
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public ProducerException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public ProducerException(final Throwable cause) {
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
	public ProducerException(final String message, final Throwable cause) {
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
	public ProducerException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
