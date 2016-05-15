package com.twitterRecommender.api.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.twitterRecommender.datamodel.BestTime;
import com.twitterRecommender.exception.RecommenderNonRetryableException;
import com.twitterRecommender.exception.RecommenderRetryableException;
import com.twitterRecommender.sao.TwitterSao;
import com.twitterRecommender.utils.CalendarUtil;

@RunWith(MockitoJUnitRunner.class)
public class TimeFinderImplTest {

	@Mock
	private TwitterSao mkTwitterSao;

	@Mock
	private CalendarUtil mkCalendarUtil;

	@InjectMocks
	private TimeFinderImpl timeFinder;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Negative testcase with null input
	 */
	@Test
	public void test_findBestTime_NullInput() {

		assertThatThrownBy(() -> {timeFinder.findBestTime(null);})
			.isInstanceOf(NullPointerException.class)
			.hasMessage("User id cannot be null");
	}

	/**
	 * Testcase for no followers
	 * 
	 * @throws RecommenderNonRetryableException
	 * @throws RecommenderRetryableException
	 */
	@Test
	public void test_findBestTime_NoFollowers() throws RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;

		when(mkTwitterSao.findFollowers(dummyUserId)).thenReturn(new ArrayList<Long>());

		BestTime expectedOutput = new BestTime(-1, -1);
		BestTime actualOutput = timeFinder.findBestTime(dummyUserId);

		Assert.assertEquals(expectedOutput, actualOutput);
	}

	/**
	 * Positive Testcase
	 * 
	 * @throws RecommenderNonRetryableException
	 * @throws RecommenderRetryableException
	 */
	@Test
	public void test_findBestTime() throws RecommenderRetryableException, RecommenderNonRetryableException {
		long dummyUserId = 1234l;

		long dummyFollower1 = 1001l;
		long dummyFollower2 = 1002l;

		List<Long> mkFollowerList = new ArrayList<>();
		mkFollowerList.add(dummyFollower1);
		mkFollowerList.add(dummyFollower2);

		Calendar cal1 = getCalender(20, 3);
		Calendar cal2 = getCalender(10, 1);
		Calendar cal3 = getCalender(10, 2);
		Calendar cal4 = getCalender(6, 2);

		List<Calendar> mktimelineFollower1 = new ArrayList<>();
		mktimelineFollower1.add(cal1);
		mktimelineFollower1.add(cal2);
		mktimelineFollower1.add(cal3);

		List<Calendar> mktimelineFollower2 = new ArrayList<>();
		mktimelineFollower2.add(cal3);
		mktimelineFollower2.add(cal4);
		
		when(mkTwitterSao.findFollowers(dummyUserId)).thenReturn(mkFollowerList);
		when(mkTwitterSao.findUserTweetTrend(dummyFollower1)).thenReturn(mktimelineFollower1);
		when(mkTwitterSao.findUserTweetTrend(dummyFollower2)).thenReturn(mktimelineFollower2);

		when(mkCalendarUtil.findHourDistribution(anyList())).thenCallRealMethod();
		when(mkCalendarUtil.findWeekDistribution(anyList())).thenCallRealMethod();

		BestTime expectedOutput = new BestTime(10, 1);
		BestTime actualOutput = timeFinder.findBestTime(dummyUserId);

		Assert.assertEquals(expectedOutput, actualOutput);
	}

	private Calendar getCalender(int hourOfDay, int dayOfWeek) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return calendar;
	}

	
}
