package com.pangratz.memorablepw.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Configuration {

	@Persistent
	private int nextPasswordLength;

	@PrimaryKey
	@Persistent
	private String lang;

	public String getLang() {
		return lang;
	}

	public int getNextPasswordLength() {
		return nextPasswordLength;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setNextPasswordLength(int nextPasswordLength) {
		this.nextPasswordLength = nextPasswordLength;
	}

}
