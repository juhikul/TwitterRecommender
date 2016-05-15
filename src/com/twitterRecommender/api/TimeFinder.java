package com.twitterRecommender.api;

import com.twitterRecommender.datamodel.BestTime;
import com.twitterRecommender.exception.RecommenderNonRetryableException;
import com.twitterRecommender.exception.RecommenderRetryableException;

/**
 * Interface to expose APIs for functionalities provided by Time Recommender
 * system.
 * 
 * @author juhi
 *
 */
public interface TimeFinder {

	/**
	 * This api accepts a user id as input and calculates and recommends time at
	 * which user should post
	 * 
	 * @param userId
	 *            - userId for whom time is to be predicted
	 * @return BestTime
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	public BestTime findBestTime(Long userId) throws RecommenderRetryableException, RecommenderNonRetryableException;
}
