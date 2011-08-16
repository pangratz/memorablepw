package com.pangratz.memorablepw.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Configuration {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private int nextPasswordLength;

	public Long getKey() {
		return key;
	}

	public int getNextPasswordLength() {
		return nextPasswordLength;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public void setNextPasswordLength(int nextPasswordLength) {
		this.nextPasswordLength = nextPasswordLength;
	}

}
