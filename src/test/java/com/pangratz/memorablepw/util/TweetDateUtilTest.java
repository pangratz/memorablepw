package com.pangratz.memorablepw.util;

import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import com.pangratz.memorablepw.util.TweetDateUtil;

public class TweetDateUtilTest extends TestCase {

	private TweetDateUtil tweetDateUtil;

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
