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
		return getNextPassword(length, null);
	}

	public Password getNextPassword(int length, String lang) {
		int _length = Math.max(length, 0);
		String _lang = (lang != null) ? lang : "en";

		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query query = pm.newQuery(Password.class, "length >= lengthParam && lang == langParam");
			query.declareParameters("Integer lengthParam, String langParam");
			query.setOrdering("length ascending, created ascending");
			query.setRange(0, 1);
			List<Password> passwords = (List<Password>) query.execute(_length, _lang);
			if (passwords != null && passwords.size() == 1) {
				return passwords.get(0);
			}
			if (_length != 0) {
				return getNextPassword(0, lang);
			}
			return null;
		} finally {
			pm.close();
		}
	}

	public int getNextPasswordLength() {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query configQuery = pm.newQuery(Configuration.class);
			configQuery.setUnique(true);
			Object object = configQuery.execute();
			if (object == null) {
				createConfigurationObject();
				return getNextPasswordLength();
			}
			Configuration config = (Configuration) object;
			return config.getNextPasswordLength();
		} finally {
			pm.close();
		}
	}

	public Statistic getStatistic() {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query query = pm.newQuery(Password.class, "length == lengthParam");
			query.declareParameters("int lengthParam");
			query.setResult("count(length)");

			Statistic statistic = new Statistic();
			int overallPasswordCount = 0;
			for (int i = 8; i <= 31; i++) {
				Integer count = (Integer) query.execute(i);
				statistic.setPasswordCount(i, count.intValue());
				overallPasswordCount += count.intValue();
			}
			statistic.setOverallPasswordsCount(overallPasswordCount);
			return statistic;
		} finally {
			pm.close();
		}
	}

	public void removePassword(String password) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query query = pm.newQuery(Password.class, "text == passwordParam");
			query.declareParameters("String passwordParam");
			query.deletePersistentAll(password);
		} finally {
			pm.close();
		}
	}

	public void updateNextPasswordLength() {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query configQuery = pm.newQuery(Configuration.class);
			configQuery.setUnique(true);
			Object object = configQuery.execute();
			Configuration config = null;
			if (object == null) {
				config = createConfigurationObject();
			} else {
				config = (Configuration) object;
			}
			int passwordLength = config.getNextPasswordLength();
			passwordLength++;
			passwordLength = (passwordLength > 31) ? 8 : passwordLength;
			config.setNextPasswordLength(passwordLength);
			pm.makePersistent(config);
		} finally {
			pm.close();
		}
	}

	private Configuration createConfigurationObject() {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Configuration configuration = new Configuration();
			configuration.setNextPasswordLength(8);
			return pm.makePersistent(configuration);
		} finally {
			pm.close();
		}
	}
}
