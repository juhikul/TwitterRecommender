package com.twitterRecommender.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Base64;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * This factory return client to connect with Twitter api
 * 
 * @author jkulshre
 *
 */
@Configuration
public class TwitterClientFactory {

	/**
	 * TODO: These configurations should be read from DB.
	 * Currently keeping then encoded here
	 */
	private final static String CONSUMER_KEY = "T1RrR3I1ZXNUS3ZzTWxGZWR6U3JFam5Icg==";
	private final static String CONSUMER_SECRET = "ZTEzb3draUZoZW1VY0h2ZmZpcnBmSnRhUlJUOGFBdzJmOFVKcmVJVDZsV3NrZWc2N3Q=";

	private final static String ACCESS_TOKEN = "MjIwMDg2NzE4OC1oS2M1bjRDRnZEU2JkNTVSRUpYQnNCNzhkSFJpYjhuY1pkZzNyQ0Q=";
	private final static String ACCESS_TOKEN_SECRET = "T2E1d1FwTlVqNUNHR1JqallUSnphSDFORTFEakpqeldlMGNRelc2RkZlTzI2";

	/**
	 * Returns client to connect with twitter
	 *
	 * @return Twitter - client
	 */
	@Bean
	public Twitter getConnection() {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(decode(CONSUMER_KEY), decode(CONSUMER_SECRET));

		AccessToken accessToken = new AccessToken(decode(ACCESS_TOKEN), decode(ACCESS_TOKEN_SECRET));
		twitter.setOAuthAccessToken(accessToken);
		return twitter;
	}

	private String decode(String key) {
		return new String(Base64.getDecoder().decode(key));
	}
}
