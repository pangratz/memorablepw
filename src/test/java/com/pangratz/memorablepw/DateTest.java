package com.pangratz.memorablepw;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;

public class DateTest extends TestCase {

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
		MutableDateTime now = new MutableDateTime(2011, 8, 22, 15, minuteParam, 0, 0, DateTimeZone.UTC);
		int origMinute = now.getMinuteOfHour();

		// adding 1ms guarantees that the exact edge cases are rounded up
		MutableDateTime nextInterval = now.millisOfSecond().add(1).minuteOfHour().roundCeiling();
		System.out.println(nextInterval);
		int minute = nextInterval.minuteOfHour().get();
		// [0,9] --> 10
		// [10,19] --> 20
		// ...
		// [50,59] --> 00
		int nextMinute = (((minute - 1) / 10) + 1) * 10;
		int add = (origMinute == 59) ? 0 : (nextMinute - minute);

		return nextInterval.minuteOfHour().add(add).toDateTime();
	}
}
