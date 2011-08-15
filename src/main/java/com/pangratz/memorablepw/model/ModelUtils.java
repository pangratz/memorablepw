package com.pangratz.memorablepw.model;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public class ModelUtils {

	private static final ModelUtils INSTANCE = new ModelUtils();

	public static ModelUtils getInstance() {
		return INSTANCE;
	}

	private PersistenceManagerFactory mPMF;

	private ModelUtils() {
		super();
		mPMF = PMF.get();
	}

	public void addPasswords(List<Password> passwords) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			pm.makePersistentAll(passwords);
		} finally {
			pm.close();
		}
	}

	public Password getNextPassword(int length) {
		int _length = Math.max(length, 0);

		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query query = pm.newQuery(Password.class, "length == lengthParam");
			query.declareParameters("Integer lengthParam");
			query.setRange(0, 1);
			List<Password> passwords = (List<Password>) query.execute(_length);
			if (passwords != null && passwords.size() == 1) {
				return passwords.get(0);
			}
			return null;
		} finally {
			pm.close();
		}
	}

	public void removePassword(String password) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query query = pm.newQuery(Password.class, "password == passwordParam");
			query.declareParameters("String passwordParam");
			query.deletePersistentAll(password);
		} finally {
			pm.close();
		}
	}
}
