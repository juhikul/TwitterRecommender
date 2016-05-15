package com.twitterRecommender.sao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twitterRecommender.exception.RecommenderNonRetryableException;
import com.twitterRecommender.exception.RecommenderRetryableException;
import com.twitterRecommender.utils.CalendarUtil;
import com.twitterRecommender.utils.Constants;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * SAO layer to interact with Twitter API
 * 
 * TODO: Find more precise checks for throwing Retryable and non-retryable
 * exceptions.
 * TODO: Add retry strategy for retryable exception with back-offs
 * 
 * Note: Due to Twitter API limitations, we get throttling exceptions.
 * 
 * @author juhi
 *
 */
@Component
public class TwitterSao {
	final static Logger LOG = Logger.getLogger(TwitterSao.class);
	
	@Autowired
	private Twitter twitter;
	
	@Autowired
	private CalendarUtil calendarUtil;

	/**
	 * For a given userId, verify if its a valid user.
	 * 
	 * @param userId
	 * @return boolean
	 */
	public boolean isValidUser(long userId) {
		boolean isValidUser = true;

		try {
			twitter.showUser(userId);
		} catch (TwitterException e) {
			isValidUser = false;
		}

		LOG.info(String.format("Userid %d is valid? %s", userId, isValidUser));
		return isValidUser;
	}

	/**
	 * For a given userId, find list of all followers
	 * 
	 * @param userId
	 * @return list of userIds of each follower
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	public List<Long> findFollowers(long userId)
			throws RecommenderRetryableException, RecommenderNonRetryableException {

		List<Long> followersList = new ArrayList<>();
		IDs ids;
		long cursor = -1;

		try {
			do {
				ids = twitter.getFollowersIDs(userId, cursor, Constants.MAX_FOLLOWER_ID);
				followersList.addAll(Arrays.asList(ArrayUtils.toObject(ids.getIDs())));
			} while ((cursor = ids.getNextCursor()) != 0);
		} catch (TwitterException e) {
			LOG.error("TwitterException while accessing twitter api for followerslist " + e.getMessage(), e);
			throw new RecommenderRetryableException("TwitterException while accessing twitter api for followerslist", e);
		} catch (Exception e) {
			LOG.error("Exception while fetching users followerslist " + e.getMessage(), e);
			throw new RecommenderNonRetryableException("Exception while fetching users followerslist",e);
		}

		LOG.info(String.format("UserId %d has %d followers", userId, followersList.size()));
		return followersList;
	}

	/**
	 * For a given userId, find timestamp of tweets made by the user. Range
	 * limitation is number tweets to a max of 1000 or tweets made in last one
	 * month, which ever is less.
	 * 
	 * @param userid
	 * @return list of Calender dates
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	public List<Calendar> findUserTweetTrend(long userid)
			throws RecommenderRetryableException, RecommenderNonRetryableException {

		List<Calendar> dateList = new ArrayList<>();

		try {
			User user = twitter.showUser(userid);

			if (!user.isProtected()) {
				Paging paging = new Paging();
				paging.setCount(Constants.MAX_USERTIMELINE_COUNT);

				Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				calendar.setTime(new Date());
				calendar.add(Calendar.MONTH, Constants.MONTH_RANGE_START_FROM);

				ResponseList<Status> statusList = twitter.getUserTimeline(userid, paging);
				dateList = statusList.stream().filter(status -> status.getCreatedAt().after(calendar.getTime()))
						.map(status -> calendarUtil.getCalenderFromDateInUtc(status.getCreatedAt()))
						.collect(Collectors.toList());
			} else {
				LOG.warn(String.format("UserId %d is a protected user", userid));
			}
		} catch (TwitterException e) {
			LOG.error("TwitterException while fetching users timeline " + e.getMessage(), e);
			throw new RecommenderRetryableException("TwitterException while fetching users timeline ", e);
		} catch (Exception e) {
			LOG.error("Exception while fetching users timeline " + e.getMessage(), e);
			throw new RecommenderNonRetryableException("Exception while fetching users timeline ", e);
		}

		LOG.info(String.format("User Id %d found %d timeline entries to process", userid, dateList.size()));

		return dateList;
	}

	/**
	 * For a given userId, find if a user exists.
	 * 
	 * @param userId
	 * @return User
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	public User findUser(long userId) throws RecommenderRetryableException, RecommenderNonRetryableException {
		User user = null;

		try {
			user = twitter.showUser(userId);
		} catch (TwitterException e) {

			// 404 is NotFound exception which would come if a user doesn't
			// exist for given userId.
			// Ref: https://dev.twitter.com/overview/api/response-codes
			if (404 == e.getStatusCode()) {
				LOG.warn(String.format("Looks like the user doesn't exist %d", userId));
				throw new RecommenderNonRetryableException("Looks like the user doesn't exist" + userId, e);
			}

			LOG.error("TwitterException while accessing twitter api for showUser. " + e.getMessage(), e);
			throw new RecommenderRetryableException("TwitterException while accessing twitter api for showUser. ", e);
		} catch (Exception e) {
			LOG.error("Exception while fetching users showUser " + e.getMessage(), e);
			throw new RecommenderNonRetryableException("Exception while fetching users showUser ", e);
		}
		return user;
	}

}
