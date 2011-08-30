package com.pangratz.memorablepw.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PasswordCounter {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String lang;

	@Persistent
	private long count;

	@Persistent
	private int length;

	public void decrement() {
		this.count--;
	}

	public long getCount() {
		return count;
	}

	public Key getKey() {
		return key;
	}

	public String getLang() {
		return lang;
	}

	public int getLength() {
		return length;
	}

	public boolean increment(long inc) {
		this.count += inc;
		return (inc > 0);
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
