package com.twitterRecommender.utils;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class CalendarUtilTest {

	/**
	 * Negative testcase for findHourDistribution
	 */
	@Test
	public void test_findHourDistribution_NullInput() {
		assertThatThrownBy(() -> {new CalendarUtil().findHourDistribution(null);})
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Calender list cannot be null");
	}

	/**
	 * Positive testcase for findHourDistribution
	 */
	@Test
	public void test_findHourDistribution_ValidInput() {
		List<Calendar> input = new ArrayList<>();

		Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal1.set(Calendar.HOUR_OF_DAY, 10);

		Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal2.set(Calendar.HOUR_OF_DAY, 5);

		Calendar cal3 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal3.set(Calendar.HOUR_OF_DAY, 5);

		input.add(cal1);
		input.add(cal2);
		input.add(cal3);

		int[] actualOutput = new CalendarUtil().findHourDistribution(input);
		
		int[] expectedOutput = new int[24];
		expectedOutput[10] = 1;
		expectedOutput[5] = 2;
		
		Assert.assertArrayEquals(expectedOutput, actualOutput);
	}

	/**
	 * Negative testcase for findWeekDistribution
	 */
	@Test
	public void test_findWeekDistribution_NullInput() {
		assertThatThrownBy(() -> {new CalendarUtil().findWeekDistribution(null);})
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Calender list cannot be null");
	}

	/**
	 * Positive testcase for findWeekDistribution
	 */
	@Test
	public void test_findWeekDistribution_ValidInput() {
		List<Calendar> input = new ArrayList<>();

		Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal1.set(Calendar.DAY_OF_WEEK, 2);

		Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal2.set(Calendar.DAY_OF_WEEK, 5);

		Calendar cal3 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal3.set(Calendar.DAY_OF_WEEK, 5);

		input.add(cal1);
		input.add(cal2);
		input.add(cal3);

		int[] actualOutput = new CalendarUtil().findWeekDistribution(input);
		
		int[] expectedOutput = new int[7];
		expectedOutput[1] = 1;
		expectedOutput[4] = 2;
		
		Assert.assertArrayEquals(expectedOutput, actualOutput);
	}

	/**
	 * Negative testcase for getCalenderFromDateInUtc
	 */
	@Test
	public void test_getCalenderFromDateInUtc_NullInput() {
		assertThatThrownBy(() -> {new CalendarUtil().getCalenderFromDateInUtc(null);})
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Date cannot be null");
	}

	/**
	 * Positive testcase for getCalenderFromDateInUtc
	 */
	@Test
	public void test_getCalenderFromDateInUtc_ValidInput() {
		Date date = new Date();
		Calendar expectedOutput = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		expectedOutput.setTime(date);

		Calendar actualOutput = new CalendarUtil().getCalenderFromDateInUtc(date);
		
		Assert.assertEquals(expectedOutput, actualOutput);
	}
}
