package com.pangratz.memorablepw.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import junit.framework.TestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class ModelUtilsTest extends TestCase {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private ModelUtils modelUtils = null;
	private PersistenceManager pm;

	public void testAddPasswords() {
		Password pw1 = createPassword("abc");
		Password pw2 = createPassword("abcd");
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(pw1);
		passwords.add(pw2);

		modelUtils.addPasswords(passwords);

		Query query = pm.newQuery(Password.class);
		List<Password> modelPws = (List<Password>) query.execute();

		assertNotNull(modelPws);
		assertEquals(2, modelPws.size());
	}

	public void testAddSamePasswords() {
		Password pw1 = createPassword("abcd");
		Password pw2 = createPassword("abcd");
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(pw1);
		passwords.add(pw2);

		modelUtils.addPasswords(passwords);

		Query query = pm.newQuery(Password.class);
		List<Password> modelPws = (List<Password>) query.execute();

		assertNotNull(modelPws);
		assertEquals(1, modelPws.size());
	}

	public void testGetGermanPassword() {
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(createPassword("hello"));
		passwords.add(createPassword("hi"));
		passwords.add(createPassword("hallo", "de"));

		modelUtils.addPasswords(passwords);

		Password password = modelUtils.getNextPassword(5, "de");
		assertNotNull(password);
		assertEquals("hallo", password.getText());
		assertEquals(5, password.getLength());
	}

	public void testGetNextAvailablePassword() {
		Password pw1 = createPassword("abc");
		Password pw2 = createPassword("abcd");
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(pw1);
		passwords.add(pw2);

		modelUtils.addPasswords(passwords);

		Password password = modelUtils.getNextPassword(2);
		assertNotNull(password);
		assertEquals("abc", password.getText());
		assertEquals(3, password.getLength());
	}

	public void testGetNextAvailablePassword2() {
		Password pw1 = createPassword("abc");
		Password pw2 = createPassword("abcd");
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(pw1);
		passwords.add(pw2);

		modelUtils.addPasswords(passwords);

		Password password = modelUtils.getNextPassword(5);
		assertNotNull(password);
		assertEquals("abc", password.getText());
		assertEquals(3, password.getLength());
	}

	public void testGetNextPasswordLength() {
		Configuration config = new Configuration();
		config.setNextPasswordLength(10);
		pm.makePersistent(config);

		int _length = modelUtils.getNextPasswordLength();
		assertEquals(10, _length);
	}

	public void testGetNextPasswordLengthInitial() {
		int _length = modelUtils.getNextPasswordLength();
		assertEquals(8, _length);
	}

	public void testGetPassword() {
		Password pw1 = createPassword("abc");
		Password pw2 = createPassword("abcd");
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(pw1);
		passwords.add(pw2);

		modelUtils.addPasswords(passwords);

		Password password = modelUtils.getNextPassword(3);
		assertNotNull(password);
		assertEquals("abc", password.getText());
		assertEquals(3, password.getLength());
	}

	public void testNotAvailablePassword() {
		Password password = modelUtils.getNextPassword(4);
		assertNull(password);
	}

	public void testRemovePassword() {
		Password pw1 = createPassword("abc");
		Password pw2 = createPassword("abcd");
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(pw1);
		passwords.add(pw2);

		modelUtils.addPasswords(passwords);
		modelUtils.removePassword("abc");

		Query query = pm.newQuery(Password.class);
		List<Password> modelPws = (List<Password>) query.execute();

		assertNotNull(modelPws);
		assertEquals(1, modelPws.size());
		assertEquals("abcd", modelPws.get(0).getText());
	}

	public void testStatistics() {
		List<Password> passwords = new LinkedList<Password>();
		passwords.add(createPassword("abcabcabc"));
		passwords.add(createPassword("defdefdef"));
		passwords.add(createPassword("ghighighi"));
		passwords.add(createPassword("aaaaaaaaaaaa"));
		passwords.add(createPassword("bbbbbbbbbbbb"));
		modelUtils.addPasswords(passwords);

		Statistic statistic = modelUtils.getStatistic();
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
	}

	public void testStatisticsGetIndex() {
		Statistic stat = new Statistic();
		assertEquals(0, stat.getIndex(7));
		assertEquals(0, stat.getIndex(8));
		assertEquals(23, stat.getIndex(31));
		assertEquals(23, stat.getIndex(32));
	}

	public void testUpdateNextPasswordLength() {
		assertEquals(8, modelUtils.getNextPasswordLength());
		modelUtils.updateNextPasswordLength();
		assertEquals(9, modelUtils.getNextPasswordLength());
	}

	public void testUpdateNextPasswordLengthOverflow() {
		Configuration configuration = new Configuration();
		configuration.setNextPasswordLength(31);
		pm.makePersistent(configuration);

		assertEquals(31, modelUtils.getNextPasswordLength());
		modelUtils.updateNextPasswordLength();
		assertEquals(8, modelUtils.getNextPasswordLength());
	}

	private Password createPassword(String string) {
		return new Password(string);
	}

	private Password createPassword(String string, String lang) {
		return new Password(string, lang);
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
