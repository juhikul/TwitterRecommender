package com.twitterRecommender.exception;

/**
 * This exception is thrown by SAO when it interacts with service.
 * This is a non-retryable exception
 *
 * TODO: Add logic to implement retryability with appropriate back-offs.
 * 
 * @author juhi
 *
 */
public class RecommenderNonRetryableException extends Exception {

	private static final long serialVersionUID = -6606970547775527813L;

	public RecommenderNonRetryableException() {
		super();
	}

	public RecommenderNonRetryableException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RecommenderNonRetryableException(final String message) {
		super(message);
	}

	public RecommenderNonRetryableException(final Throwable cause) {
		super(cause);
	}
}
