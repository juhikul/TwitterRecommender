package com.twitterRecommender.datamodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import twitter4j.User;

/**
 * Data model to hold response for best time finder
 * 
 * @author juhi
 *
 */
@Getter
@AllArgsConstructor
@ToString
public class TimeFinderResponse {

	@NonNull
	private User user;

	@NonNull
	private BestTime bestTime;
}
