package com.twitterRecommender.api.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twitterRecommender.api.TimeFinder;
import com.twitterRecommender.datamodel.BestTime;
import com.twitterRecommender.exception.RecommenderNonRetryableException;
import com.twitterRecommender.exception.RecommenderRetryableException;
import com.twitterRecommender.sao.TwitterSao;
import com.twitterRecommender.utils.CalendarUtil;

/**
 * Implementation class for TimeFinder.
 * This holds logic to calculate best time for posting
 *
 * @author juhi
 *
 */
@Component
public class TimeFinderImpl implements TimeFinder {
	final static Logger LOG = Logger.getLogger(TimeFinderImpl.class);

	@Autowired
	private TwitterSao twitterSao;

	@Autowired
	private CalendarUtil calendarUtil;

	@Override
	public BestTime findBestTime(Long userId) throws RecommenderRetryableException, RecommenderNonRetryableException {
		Validate.notNull(userId, "User id cannot be null");

		/**
		 * Step 1. find followers list for the user
		 * Step 2. find latest tweet time for the user
		 * Step 3. find time of day, day of week
		 * Step 4. model into response and return
		 */

		int[] timeOfDay = new int[24];
		int[] dayOfWeek = new int[7];


		// Step 1. find followers list for the user
		List<Long> followersIdList = twitterSao.findFollowers(userId);
		
		for (long follewerId : followersIdList) {
			
			// Step 2. find latest tweet time for the user
			List<Calendar> timeline = twitterSao.findUserTweetTrend(follewerId);
			
			// Step 3. find time of day, day of week
			int[] hourlyTrend = calendarUtil.findHourDistribution(timeline);
			int[] weeklyTrend = calendarUtil.findWeekDistribution(timeline);
			
			for (int i=0; i<hourlyTrend.length; i++) {
				timeOfDay[i] += hourlyTrend[i];
			}

			for (int i=0; i<weeklyTrend.length; i++) {
				dayOfWeek[i] += weeklyTrend[i];
			}
		}
		
		// Step 4. model into response and return
		BestTime bestTime = new BestTime(maxIndexOfArray(timeOfDay), maxIndexOfArray(dayOfWeek));
		return bestTime;
	}

	private int maxIndexOfArray(int[] array) {
		int maxVal = 0;
		int index = -1;

		for (int i=0; i< array.length; i++) {
			if (array[i] > maxVal) {
				maxVal = array[i];
				index = i;
			}
		}
		return index;
	}

}
