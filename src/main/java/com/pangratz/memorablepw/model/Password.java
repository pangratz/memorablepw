package com.pangratz.memorablepw.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Password {

	@PrimaryKey
	@Persistent
	private String text;

	@Persistent
	private int length;

	@Persistent
	private String lang;

	@Persistent
	private long created;

	public Password() {
		super();
		this.created = System.currentTimeMillis();
	}

	public Password(String pw) {
		this(pw, "en");
	}

	public Password(String pw, String lang) {
		this();
		this.text = pw;
		if (pw != null) {
			this.length = pw.length();
		}
		this.lang = lang;
	}

	public long getCreated() {
		return created;
	}

	public String getLang() {
		return lang;
	}

	public int getLength() {
		return length;
	}

	public String getText() {
		return text;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setText(String password) {
		this.text = password;
	}

}
