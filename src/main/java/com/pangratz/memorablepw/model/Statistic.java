package com.pangratz.memorablepw.model;

public class Statistic {

	// 31 - 8 + 1 = 24
	private int[] counts = new int[24];
	private int overallPasswordsCount;
	private final String lang;

	public Statistic(String lang) {
		super();
		this.lang = lang;
	}

	public int getIndex(int length) {
		// project length to an interval between 0 <= index <= 23
		return Math.min(counts.length - 1, Math.max(length - 8, 0));
	}

	public String getLang() {
		return lang;
	}

	public int getOverallPasswordsCount() {
		return overallPasswordsCount;
	}

	public int getPasswordsCount(int length) {
		return counts[getIndex(length)];
	}

	public void setOverallPasswordsCount(int overallPasswordsCount) {
		this.overallPasswordsCount = overallPasswordsCount;
	}

	public void setPasswordCount(int length, int count) {
		counts[getIndex(length)] = count;
	}

}
