package com.twitterRecommender.datamodel;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Data model to hold best time entities
 * 
 * @author juhi
 *
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BestTime {

	private int timeInHour;
	private int timeInWeek;
}
