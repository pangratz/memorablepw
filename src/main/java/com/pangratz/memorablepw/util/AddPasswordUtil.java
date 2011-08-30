package com.pangratz.memorablepw.util;

import java.util.LinkedList;
import java.util.List;

import com.pangratz.memorablepw.model.Password;
import com.pangratz.memorablepw.model.PasswordCounter;

public class AddPasswordUtil {

	private final long[] enCounter;
	private final long[] deCounter;
	private final List<Password> passwords;

	public AddPasswordUtil() {
		super();

		int size = 31 - 8 + 1;
		enCounter = new long[size];
		deCounter = new long[size];

		passwords = new LinkedList<Password>();
	}

	public boolean applyUpdateCount(PasswordCounter passwordCounter) {
		String lang = passwordCounter.getLang();
		int index = passwordCounter.getLength() - 8;
		if ("en".equals(lang)) {
			return passwordCounter.increment(enCounter[index]);
		} else if ("de".equals(lang)) {
			return passwordCounter.increment(deCounter[index]);
		}
		return false;
	}

	public Password c(String pw, String lang) {
		Password p = new Password(pw, lang);
		int index = p.getLength() - 8;
		if ("en".equals(lang))
			enCounter[index]++;
		else if ("de".equals(lang))
			deCounter[index]++;
		else
			throw new IllegalStateException("invalid language: " + lang);

		return p;
	}

	public List<Password> getPasswords() {
		return passwords;
	}

}
