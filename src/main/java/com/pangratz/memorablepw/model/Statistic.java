package com.pangratz.memorablepw.model;

public class Statistic {

	// 31 - 8 + 1 = 24
	private int[] counts = new int[24];

	public int getIndex(int length) {
		// project length to an interval between 0 <= index <= 23
		return Math.min(counts.length - 1, Math.max(length - 8, 0));
	}

	public int getPasswordsCount(int length) {
		return counts[getIndex(length)];
	}

	public void setPasswordCount(int length, int count) {
		counts[getIndex(length)] = count;
	}

}