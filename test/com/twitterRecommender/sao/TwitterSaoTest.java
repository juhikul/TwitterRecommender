package com.twitterRecommender.sao;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.twitterRecommender.exception.RecommenderNonRetryableException;
import com.twitterRecommender.exception.RecommenderRetryableException;
import com.twitterRecommender.utils.CalendarUtil;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

@RunWith(MockitoJUnitRunner.class)
public class TwitterSaoTest {

	@Mock
	private Twitter mkTwitter;
	
	@Mock
	private CalendarUtil mkCalendarUtil;

	@Mock
	private IDs mkIds;

	@Mock
	private User mkUser;

	@InjectMocks
	private TwitterSao twitterSao;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Negative testcase for isValidUser
	 * 
	 * @throws TwitterException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_isValidUser_InvalidUser() throws TwitterException {
		long dummyUserId = 1234l;

		when(mkTwitter.showUser(dummyUserId)).thenThrow(TwitterException.class);
		
		boolean actualOutput = twitterSao.isValidUser(dummyUserId);

		Assert.assertFalse(actualOutput);
	}

	/**
	 * Positive testcase for isValidUser
	 * 
	 * @throws TwitterException 
	 */
	@Test
	public void test_isValidUser_ValidUser() throws TwitterException {
		long dummyUserId = 1234l;
		
		boolean actualOutput = twitterSao.isValidUser(dummyUserId);

		Assert.assertTrue(actualOutput);
	}

	/**
	 * Negative testcase for findFollowers
	 * 
	 * @throws TwitterException 
	 * @throws RecommenderNonRetryableException 
	 * @throws RecommenderRetryableException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_findFollowers_ThrowRetryableException() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;

		// Throw retryable exception
		when(mkTwitter.getFollowersIDs(anyLong(), anyLong(), anyInt())).thenThrow(TwitterException.class);

		assertThatThrownBy(() -> {twitterSao.findFollowers(dummyUserId);})
			.isInstanceOf(RecommenderRetryableException.class)
			.hasMessage("TwitterException while accessing twitter api for followerslist");

	}

	/**
	 * Negative testcase for findFollowers
	 * 
	 * @throws TwitterException 
	 * @throws RecommenderNonRetryableException 
	 * @throws RecommenderRetryableException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_findFollowers_ThrowException() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;

		// Throw non retryable exception
		when(mkTwitter.getFollowersIDs(anyLong(), anyLong(), anyInt())).thenThrow(Exception.class);

		assertThatThrownBy(() -> {twitterSao.findFollowers(dummyUserId);})
			.isInstanceOf(RecommenderNonRetryableException.class)
			.hasMessage("Exception while fetching users followerslist");
	}


	/**
	 * Positive testcase
	 *
	 * @throws TwitterException
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	@Test
	public void test_findFollowers_ValidCase() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;

		List<Long> expectedOutput = new ArrayList<>();
		expectedOutput.add(123l);
		expectedOutput.add(234l);

		when(mkTwitter.getFollowersIDs(anyLong(), anyLong(), anyInt())).thenReturn(mkIds);
		when(mkIds.getIDs()).thenReturn(new long[] {123l, 234l});
		when(mkIds.getNextCursor()).thenReturn(0l);

		List<Long> actualOutput = twitterSao.findFollowers(dummyUserId);
		
		Assert.assertEquals(expectedOutput, actualOutput);

	}

	/**
	 * Negative Testcase for findUserTweetTrend
	 *
	 * @throws TwitterException
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_findUserTweetTrend_ThrowRetryaleException() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;
		
		when(mkTwitter.showUser(anyLong())).thenThrow(TwitterException.class);

		assertThatThrownBy(() -> {twitterSao.findUserTweetTrend(dummyUserId);})
			.isInstanceOf(RecommenderRetryableException.class)
			.hasMessage("TwitterException while fetching users timeline ");

	}

	/**
	 * Negative Testcase for findUserTweetTrend
	 *
	 * @throws TwitterException
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_findUserTweetTrend_ThrowNonRetryaleException() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;
		
		when(mkTwitter.showUser(anyLong())).thenThrow(Exception.class);

		assertThatThrownBy(() -> {twitterSao.findUserTweetTrend(dummyUserId);})
			.isInstanceOf(RecommenderNonRetryableException.class)
			.hasMessage("Exception while fetching users timeline ");

	}

	/**
	 * Testcase for protected user
	 *
	 * @throws TwitterException
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	@Test
	public void test_findUserTweetTrend_ProtectedUser() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;
		
		when(mkTwitter.showUser(anyLong())).thenReturn(mkUser);
		when(mkUser.isProtected()).thenReturn(Boolean.TRUE);

		Assert.assertEquals(new ArrayList<Calendar>(), twitterSao.findUserTweetTrend(dummyUserId));
	}


	/**
	 * Negative Testcase for findUser
	 *
	 * @throws TwitterException
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	@Test
	public void test_findUser_InValidUser() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;
		
		TwitterException e = new TwitterException("", new Exception(), 404);
		when(mkTwitter.showUser(anyLong())).thenThrow(e);
		
		assertThatThrownBy(() -> {twitterSao.findUser(dummyUserId);})
			.isInstanceOf(RecommenderNonRetryableException.class)
			.hasMessage("Looks like the user doesn't exist" + dummyUserId);
	}

	/**
	 * Negative Testcase for findUser
	 *
	 * @throws TwitterException
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_findUser_ThrowsRetryableException() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;
		
		when(mkTwitter.showUser(anyLong())).thenThrow(TwitterException.class);
		
		assertThatThrownBy(() -> {twitterSao.findUser(dummyUserId);})
			.isInstanceOf(RecommenderRetryableException.class)
			.hasMessage("TwitterException while accessing twitter api for showUser. ");
	}

	/**
	 * Negative Testcase for findUser
	 *
	 * @throws TwitterException
	 * @throws RecommenderRetryableException
	 * @throws RecommenderNonRetryableException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_findUser_ThrowsNonRetryableException() throws TwitterException, RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;
		
		when(mkTwitter.showUser(anyLong())).thenThrow(Exception.class);
		
		assertThatThrownBy(() -> {twitterSao.findUser(dummyUserId);})
			.isInstanceOf(RecommenderNonRetryableException.class)
			.hasMessage("Exception while fetching users showUser ");
	}
}
