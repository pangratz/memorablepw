package com.pangratz.memorablepw.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Password {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private String password;

	@Persistent
	private int length;

	public Password() {
		super();
	}

	public Password(String pw) {
		this();
		this.password = pw;
		if (pw != null) {
			this.length = pw.length();
		}
	}

	public Long getKey() {
		return key;
	}

	public int getLength() {
		return length;
	}

	public String getPassword() {
		return password;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
