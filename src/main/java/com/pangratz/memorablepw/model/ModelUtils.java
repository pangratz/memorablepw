package com.pangratz.memorablepw.model;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.pangratz.memorablepw.util.AddPasswordUtil;

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

	public void addPasswords(AddPasswordUtil apu) {
		addPasswordsToDatastore(apu.getPasswords());

		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query pcQ = pm.newQuery(PasswordCounter.class);
			List<PasswordCounter> updatedPcs = new LinkedList<PasswordCounter>();
			List<PasswordCounter> pcs = (List<PasswordCounter>) pcQ.execute();
			for (PasswordCounter passwordCounter : pcs) {
				if (apu.applyUpdateCount(passwordCounter)) {
					updatedPcs.add(passwordCounter);
				}
			}
			pm.makePersistentAll(updatedPcs);
		} finally {
			pm.close();
		}
	}

	@Deprecated
	public void addPasswords(List<Password> passwords) {
		addPasswordsToDatastore(passwords);
		updatePasswordCounters();
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

	public int getNextPasswordLength(String lang) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query configQuery = pm.newQuery(Configuration.class, "lang == langParam");
			configQuery.declareParameters("String langParam");
			configQuery.setUnique(true);
			Object object = configQuery.execute(lang);
			if (object == null) {
				createConfigurationObject(lang);
				return getNextPasswordLength(lang);
			}
			Configuration config = (Configuration) object;
			return config.getNextPasswordLength();
		} finally {
			pm.close();
		}
	}

	public Statistic getStatistic(String lang) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query query = pm.newQuery(PasswordCounter.class, "lang == langParam");
			query.declareParameters("String langParam");
			List<PasswordCounter> passwordCounters = (List<PasswordCounter>) query.execute(lang);

			Statistic statistic = new Statistic(lang);
			int overallPasswordCount = 0;
			for (PasswordCounter passwordCounter : passwordCounters) {
				long count = passwordCounter.getCount();
				int length = passwordCounter.getLength();
				statistic.setPasswordCount(length, (int) count);
				overallPasswordCount += count;
			}
			statistic.setOverallPasswordsCount(overallPasswordCount);
			return statistic;
		} finally {
			pm.close();
		}
	}

	public List<Statistic> getStatistics() {
		LinkedList<Statistic> statistics = new LinkedList<Statistic>();
		statistics.add(getStatistic("en"));
		statistics.add(getStatistic("de"));
		return statistics;
	}

	public void removePassword(String text) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query pwQuery = pm.newQuery(Password.class, "text == textParam");
			pwQuery.declareParameters("String textParam");
			pwQuery.setUnique(true);

			Password password = (Password) pwQuery.execute(text);
			String lang = password.getLang();
			int length = password.getLength();

			pm.deletePersistent(password);

			Query pcQ = pm.newQuery(PasswordCounter.class, "lang == langParam && length == lengthParam");
			pcQ.declareParameters("String langParam, int lengthParam");
			pcQ.setUnique(true);
			PasswordCounter pwCounter = (PasswordCounter) pcQ.execute(lang, length);
			pwCounter.decrement();
			pm.makePersistent(pwCounter);

		} finally {
			pm.close();
		}
	}

	public void updateNextPasswordLength(String lang) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Query configQuery = pm.newQuery(Configuration.class, "lang == langParam");
			configQuery.declareParameters("String langParam");
			configQuery.setUnique(true);
			Object object = configQuery.execute(lang);
			Configuration config = null;
			if (object == null) {
				config = createConfigurationObject(lang);
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

	public void updatePasswordCounters() {
		String[] langs = new String[] { "en", "de" };

		PersistenceManager pm = mPMF.getPersistenceManager();

		Query query = pm.newQuery(Password.class, "length == lengthParam && lang == langParam");
		query.declareParameters("int lengthParam, String langParam");
		query.setResult("count(length)");

		Query pwCounterQ = pm.newQuery(PasswordCounter.class, "length == lengthParam && lang == langParam");
		pwCounterQ.declareParameters("int lengthParam, String langParam");
		pwCounterQ.setUnique(true);

		try {
			List<PasswordCounter> pwCounters = new LinkedList<PasswordCounter>();
			for (String lang : langs) {
				for (int length = 8; length <= 31; length++) {
					Integer count = (Integer) query.execute(length, lang);
					Object pwCounterObj = pwCounterQ.execute(length, lang);
					if (pwCounterObj == null) {
						pwCounterObj = new PasswordCounter();
					}
					PasswordCounter pwCounter = (PasswordCounter) pwCounterObj;
					pwCounter.setLang(lang);
					pwCounter.setLength(length);
					pwCounter.setCount(count);
					pwCounters.add(pwCounter);
				}
			}
			pm.makePersistentAll(pwCounters);
		} finally {
			pm.close();
		}
	}

	private void addPasswordsToDatastore(List<Password> passwords) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			pm.makePersistentAll(passwords);
		} finally {
			pm.close();
		}
	}

	private Configuration createConfigurationObject(String lang) {
		PersistenceManager pm = mPMF.getPersistenceManager();
		try {
			Configuration configuration = new Configuration();
			configuration.setLang(lang);
			configuration.setNextPasswordLength(8);
			return pm.makePersistent(configuration);
		} finally {
			pm.close();
		}
	}
}
