package com.twitterRecommender.exception;

/**
 * This exception is thrown by SAO when it interacts with service.
 * This is a retryable exception
 *
 * @author juhi
 *
 */
public class RecommenderRetryableException extends Exception {

	private static final long serialVersionUID = -9209049098325699491L;

	public RecommenderRetryableException() {
		super();
	}

	public RecommenderRetryableException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RecommenderRetryableException(final String message) {
		super(message);
	}

	public RecommenderRetryableException(final Throwable cause) {
		super(cause);
	}
}
