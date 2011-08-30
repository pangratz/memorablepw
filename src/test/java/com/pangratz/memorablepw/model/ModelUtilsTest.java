package com.pangratz.memorablepw.model;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import junit.framework.TestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.pangratz.memorablepw.util.AddPasswordUtil;

public class ModelUtilsTest extends TestCase {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private ModelUtils modelUtils = null;
	private PersistenceManager pm;

	public void testAddPasswords() {
		modelUtils.updatePasswordCounters();

		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabc");
		apu.c("abcdabcdabcd");
		modelUtils.addPasswords(apu);

		Query query = pm.newQuery(Password.class);
		List<Password> modelPws = (List<Password>) query.execute();

		assertNotNull(modelPws);
		assertEquals(2, modelPws.size());
	}

	public void testAddPasswordsWithAddPasswordUtil() {
		modelUtils.updatePasswordCounters();

		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabc", "en");
		apu.c("defdefdef", "en");
		apu.c("ghighighi", "en");
		apu.c("aaaaaaaaaaaa", "en");
		apu.c("bbbbbbbbbbbb", "en");
		apu.c("abcdefgh", "de");
		apu.c("abcdefghi", "de");
		apu.c("abcdefghh", "de");
		modelUtils.addPasswords(apu);

		Statistic enStatistic = modelUtils.getStatistic("en");
		assertNotNull(enStatistic);
		for (int i = 8; i <= 31; i++) {
			if (i == 9) {
				assertEquals(3, enStatistic.getPasswordsCount(9));
			} else if (i == 12) {
				assertEquals(2, enStatistic.getPasswordsCount(12));
			} else {
				assertEquals(0, enStatistic.getPasswordsCount(i));
			}
		}
		assertEquals(5, enStatistic.getOverallPasswordsCount());

		Statistic deStatistic = modelUtils.getStatistic("de");
		assertNotNull(deStatistic);
		for (int i = 8; i <= 31; i++) {
			if (i == 8) {
				assertEquals(1, deStatistic.getPasswordsCount(8));
			} else if (i == 9) {
				assertEquals(2, deStatistic.getPasswordsCount(9));
			} else {
				assertEquals(0, deStatistic.getPasswordsCount(i));
			}
		}
		assertEquals(3, deStatistic.getOverallPasswordsCount());
	}

	public void testAddSamePasswords() {
		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcdabcd");
		apu.c("abcdabcd");
		modelUtils.addPasswords(apu);

		Query query = pm.newQuery(Password.class);
		List<Password> modelPws = (List<Password>) query.execute();

		assertNotNull(modelPws);
		assertEquals(1, modelPws.size());
	}

	public void testGetEmptyStatistics() {
		List<Statistic> statistics = modelUtils.getStatistics();
		assertNotNull(statistics);
		assertEquals(2, statistics.size());

		for (Statistic statistic : statistics) {
			for (int i = 8; i <= 31; i++) {
				assertEquals(0, statistic.getPasswordsCount(i));
			}
		}
	}

	public void testGetGermanPassword() {
		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("hellohello");
		apu.c("hihihihi");
		apu.c("hallhall", "de");
		modelUtils.addPasswords(apu);

		Password password = modelUtils.getNextPassword(8, "de");
		assertNotNull(password);
		assertEquals("hallhall", password.getText());
		assertEquals(8, password.getLength());
	}

	public void testGetNextAvailablePassword() {
		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabc");
		apu.c("abcdabcddd");
		modelUtils.addPasswords(apu);

		Password password = modelUtils.getNextPassword(8);
		assertNotNull(password);
		assertEquals("abcabcabc", password.getText());
		assertEquals(9, password.getLength());
	}

	public void testGetNextAvailablePassword2() {
		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcdabcd");
		apu.c("abcdabcda");
		modelUtils.addPasswords(apu);

		Password password = modelUtils.getNextPassword(10);
		assertNotNull(password);
		assertEquals("abcdabcd", password.getText());
		assertEquals(8, password.getLength());
	}

	public void testGetNextPasswordLength() {
		Configuration config = new Configuration();
		config.setLang("en");
		config.setNextPasswordLength(10);
		pm.makePersistent(config);

		int _length = modelUtils.getNextPasswordLength("en");
		assertEquals(10, _length);
	}

	public void testGetNextPasswordLengthInitial() {
		int _length = modelUtils.getNextPasswordLength("en");
		assertEquals(8, _length);
	}

	public void testGetPassword() {
		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabca");
		apu.c("abcdabcdasa");
		modelUtils.addPasswords(apu);

		Password password = modelUtils.getNextPassword(9);
		assertNotNull(password);
		assertEquals("abcabcabca", password.getText());
		assertEquals(10, password.getLength());
	}

	public void testGetStatistics() {
		modelUtils.updatePasswordCounters();

		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabc", "en");
		apu.c("defdefdef", "en");
		apu.c("ghighighi", "en");
		apu.c("aaaaaaaaaaaa", "en");
		apu.c("bbbbbbbbbbbb", "en");
		apu.c("abcdefgh", "de");
		apu.c("abcdefghi", "de");
		apu.c("abcdefghh", "de");
		modelUtils.addPasswords(apu);

		List<Statistic> statistics = modelUtils.getStatistics();
		assertNotNull(statistics);
		assertEquals(2, statistics.size());
		for (Statistic statistic : statistics) {
			if (statistic.getLang().equals("en")) {
				for (int i = 8; i <= 31; i++) {
					if (i == 9) {
						assertEquals(3, statistic.getPasswordsCount(9));
					} else if (i == 12) {
						assertEquals(2, statistic.getPasswordsCount(12));
					} else {
						assertEquals(0, statistic.getPasswordsCount(i));
					}
				}
				assertEquals(5, statistic.getOverallPasswordsCount());
			} else if (statistic.getLang().equals("de")) {
				for (int i = 8; i <= 31; i++) {
					if (i == 8) {
						assertEquals(1, statistic.getPasswordsCount(8));
					} else if (i == 9) {
						assertEquals(2, statistic.getPasswordsCount(9));
					} else {
						assertEquals(0, statistic.getPasswordsCount(i));
					}
				}
				assertEquals(3, statistic.getOverallPasswordsCount());
			}
		}
	}

	public void testNotAvailablePassword() {
		Password password = modelUtils.getNextPassword(4);
		assertNull(password);
	}

	public void testRemovePassword() {
		modelUtils.updatePasswordCounters();
		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabc");
		apu.c("abcdabcdabcd");
		modelUtils.addPasswords(apu);
		modelUtils.removePassword("abcabcabc");

		Query query = pm.newQuery(Password.class);
		List<Password> modelPws = (List<Password>) query.execute();

		assertNotNull(modelPws);
		assertEquals(1, modelPws.size());
		assertEquals("abcdabcdabcd", modelPws.get(0).getText());
	}

	public void testStatisticCounter() {
		List<Password> passwords = new LinkedList<Password>();
		passwords.add(createPassword("abcabcabc"));
		passwords.add(createPassword("defdefdef"));
		passwords.add(createPassword("ghighighi"));
		passwords.add(createPassword("aaaaaaaaaaaa"));
		passwords.add(createPassword("bbbbbbbbbbbb"));
		pm.makePersistentAll(passwords);
		modelUtils.updatePasswordCounters();

		Statistic enStatistic = modelUtils.getStatistic("en");
		assertEquals(3, enStatistic.getPasswordsCount(9));
		assertEquals(2, enStatistic.getPasswordsCount(12));

		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcdfghij");
		apu.c("123456789");
		modelUtils.addPasswords(apu);

		Statistic s = modelUtils.getStatistic("en");
		assertEquals(5, s.getPasswordsCount(9));
		assertEquals(2, s.getPasswordsCount(12));

		modelUtils.removePassword("abcabcabc");

		Statistic s2 = modelUtils.getStatistic("en");
		assertEquals(4, s2.getPasswordsCount(9));
		assertEquals(2, s2.getPasswordsCount(12));
	}

	public void testStatistics() {
		modelUtils.updatePasswordCounters();

		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabc");
		apu.c("defdefdef");
		apu.c("ghighighi");
		apu.c("aaaaaaaaaaaa");
		apu.c("bbbbbbbbbbbb");
		modelUtils.addPasswords(apu);

		Statistic statistic = modelUtils.getStatistic("en");
		assertNotNull(statistic);
		for (int i = 8; i <= 31; i++) {
			if (i == 9) {
				assertEquals(3, statistic.getPasswordsCount(9));
			} else if (i == 12) {
				assertEquals(2, statistic.getPasswordsCount(12));
			} else {
				assertEquals(0, statistic.getPasswordsCount(i));
			}
		}
		assertEquals(5, statistic.getOverallPasswordsCount());
	}

	public void testStatisticsGetIndex() {
		Statistic stat = new Statistic("en");
		assertEquals(0, stat.getIndex(7));
		assertEquals(0, stat.getIndex(8));
		assertEquals(23, stat.getIndex(31));
		assertEquals(23, stat.getIndex(32));
	}

	public void testStatisticsMultipleLangs() {
		modelUtils.updatePasswordCounters();

		AddPasswordUtil apu = new AddPasswordUtil();
		apu.c("abcabcabc", "en");
		apu.c("defdefdef", "en");
		apu.c("ghighighi", "en");
		apu.c("aaaaaaaaaaaa", "en");
		apu.c("bbbbbbbbbbbb", "en");
		apu.c("abcdefgh", "de");
		apu.c("abcdefghi", "de");
		apu.c("abcdefghh", "de");
		modelUtils.addPasswords(apu);

		Statistic enStatistic = modelUtils.getStatistic("en");
		assertNotNull(enStatistic);
		for (int i = 8; i <= 31; i++) {
			if (i == 9) {
				assertEquals(3, enStatistic.getPasswordsCount(9));
			} else if (i == 12) {
				assertEquals(2, enStatistic.getPasswordsCount(12));
			} else {
				assertEquals(0, enStatistic.getPasswordsCount(i));
			}
		}
		assertEquals(5, enStatistic.getOverallPasswordsCount());

		Statistic deStatistic = modelUtils.getStatistic("de");
		assertNotNull(deStatistic);
		for (int i = 8; i <= 31; i++) {
			if (i == 8) {
				assertEquals(1, deStatistic.getPasswordsCount(8));
			} else if (i == 9) {
				assertEquals(2, deStatistic.getPasswordsCount(9));
			} else {
				assertEquals(0, deStatistic.getPasswordsCount(i));
			}
		}
		assertEquals(3, deStatistic.getOverallPasswordsCount());
	}

	public void testUpdateNextPasswordLength() {
		assertEquals(8, modelUtils.getNextPasswordLength("en"));
		modelUtils.updateNextPasswordLength("en");
		assertEquals(9, modelUtils.getNextPasswordLength("en"));
	}

	public void testUpdateNextPasswordLengthOverflow() {
		Configuration configuration = new Configuration();
		configuration.setLang("en");
		configuration.setNextPasswordLength(31);
		pm.makePersistent(configuration);

		assertEquals(31, modelUtils.getNextPasswordLength("en"));
		modelUtils.updateNextPasswordLength("en");
		assertEquals(8, modelUtils.getNextPasswordLength("en"));
	}

	public void testUpdatePasswordCounters() {
		modelUtils.updatePasswordCounters();
		List<Statistic> statistics = modelUtils.getStatistics();
		assertNotNull(statistics);
	}

	private Password createPassword(String string) {
		return new Password(string);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		helper.setUp();
		pm = PMF.get().getPersistenceManager();
		modelUtils = ModelUtils.getInstance();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		helper.tearDown();
		pm.close();
		modelUtils = null;
	}

}
