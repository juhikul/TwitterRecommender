package com.twitterRecommender.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

/**
 * A utility class to hold functionalities related to Calendar
 * 
 * @author juhi
 *
 */
@Component
public class CalendarUtil {

	/**
	 * On the basis of list of Calendar events, returns the array of hour with
	 * each index holding number of calendar events in that hour
	 * 
	 * @param calendarList
	 *            - list of calendar
	 * @return int[] of size 24. Each index represents the hour
	 */
	public int[] findHourDistribution(List<Calendar> calendarList) {
		Validate.notNull(calendarList, "Calender list cannot be null");
		int[] hours = new int[24];

		for (Calendar calendar : calendarList) {
			hours[calendar.get(Calendar.HOUR_OF_DAY)]++;
		}
		return hours;
	}

	/**
	 * On the basis of list of Calendar events, returns the array of week with
	 * each index holding number of calendar events in that week
	 * 
	 * @param calendarList
	 * @return int[] of size 7. Each index represents the week
	 */
	public int[] findWeekDistribution(List<Calendar> calendarList) {
		Validate.notNull(calendarList, "Calender list cannot be null");
		int[] week = new int[7];

		for (Calendar calendar : calendarList) {
			week[calendar.get(Calendar.DAY_OF_WEEK) - 1]++;
		}
		return week;
	}

	/**
	 * Converts date into Calnder object in UTC time zone
	 * 
	 * @param date
	 * @return Calendar
	 */
	public Calendar getCalenderFromDateInUtc(Date date) {
		Validate.notNull(date, "Date cannot be null");
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		return calendar;
	}
}
