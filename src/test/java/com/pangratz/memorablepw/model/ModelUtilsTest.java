package com.pangratz.memorablepw.model;

import java.util.ArrayList;
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

	public void testGetPassword() {
		Password pw1 = createPassword("abc");
		Password pw2 = createPassword("abcd");
		List<Password> passwords = new ArrayList<Password>();
		passwords.add(pw1);
		passwords.add(pw2);

		modelUtils.addPasswords(passwords);

		Password password = modelUtils.getNextPassword(3);
		assertNotNull(password);
		assertEquals("abc", password.getPassword());
		assertEquals(3, password.getLength());
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
		assertEquals("abcd", modelPws.get(0).getPassword());
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
