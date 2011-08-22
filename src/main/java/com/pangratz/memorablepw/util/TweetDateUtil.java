package com.pangratz.memorablepw.util;

import java.util.Date;

import org.joda.time.MutableDateTime;

public class TweetDateUtil {

	private static final TweetDateUtil INSTANCE = new TweetDateUtil();

	public static TweetDateUtil getInstance() {
		return INSTANCE;
	}

	public Date getNextTweetDate() {
		return getNextTweetDate(new Date());
	}

	public Date getNextTweetDate(Date startDate) {
		MutableDateTime now = new MutableDateTime(startDate);
		int origMinute = now.getMinuteOfHour();

		// adding 1ms guarantees that the exact edge cases are rounded up
		MutableDateTime nextInterval = now.millisOfSecond().add(1).minuteOfHour().roundCeiling();
		int minute = nextInterval.minuteOfHour().get();
		// [0,9] --> 10
		// [10,19] --> 20
		// ...
		// [50,59] --> 00
		int nextMinute = (((minute - 1) / 10) + 1) * 10;
		int add = (origMinute == 59) ? 0 : (nextMinute - minute);

		return nextInterval.minuteOfHour().add(add).toDate();
	}

}
