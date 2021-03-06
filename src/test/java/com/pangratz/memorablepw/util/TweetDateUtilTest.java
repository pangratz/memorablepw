package com.pangratz.memorablepw.util;

import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.PeriodType;

public class TweetDateUtilTest extends TestCase {

	private TweetDateUtil tweetDateUtil;

	public void testGetLastTweetDate() {
		DateTime startDate = new DateTime(2011, 8, 22, 15, 15, 0, DateTimeZone.UTC);

		Date inTwoTweetsDate = tweetDateUtil.getLastTweetDate(startDate.toDate(), 2);
		assertEquals(new DateTime(2011, 8, 22, 15, 30, 0, DateTimeZone.UTC).getMillis(), inTwoTweetsDate.getTime());

		Date inSixTweetsDate = tweetDateUtil.getLastTweetDate(startDate.toDate(), 6);
		assertEquals(new DateTime(2011, 8, 22, 16, 10, 0, DateTimeZone.UTC).getMillis(), inSixTweetsDate.getTime());

		DateTime lastTweetDate = new DateTime(tweetDateUtil.getLastTweetDate(566), DateTimeZone.UTC);
		lastTweetDate = lastTweetDate.toDateTime(DateTimeZone.forOffsetHours(2));
		System.out.println(lastTweetDate);

		Interval interval = new Interval(new DateTime(DateTimeZone.UTC), lastTweetDate);
		System.out.println(interval.toPeriod(PeriodType.dayTime()));
	}

	public void testRounding() {
		assertEquals(10, roundMinute(0).minuteOfHour().get());
		assertEquals(10, roundMinute(1).minuteOfHour().get());
		assertEquals(10, roundMinute(9).minuteOfHour().get());
		assertEquals(20, roundMinute(10).minuteOfHour().get());
		assertEquals(20, roundMinute(11).minuteOfHour().get());
		assertEquals(20, roundMinute(19).minuteOfHour().get());
		assertEquals(30, roundMinute(20).minuteOfHour().get());
		assertEquals(30, roundMinute(29).minuteOfHour().get());
		assertEquals(50, roundMinute(49).minuteOfHour().get());
		assertEquals(0, roundMinute(50).minuteOfHour().get());
		assertEquals(0, roundMinute(58).minuteOfHour().get());

		DateTime trickyEdge = roundMinute(59);
		assertEquals(0, trickyEdge.minuteOfHour().get());
		assertEquals(16, trickyEdge.hourOfDay().get());
	}

	private DateTime roundMinute(int minuteParam) {
		DateTime startDate = new DateTime(2011, 8, 22, 15, minuteParam, 0);
		Date nextTweetDate = tweetDateUtil.getNextTweetDate(startDate.toDate());
		return new DateTime(nextTweetDate);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.tweetDateUtil = TweetDateUtil.getInstance();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.tweetDateUtil = null;
	}
}
