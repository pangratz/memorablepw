package com.pangratz.memorablepw.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.pangratz.memorablepw.model.ModelUtils;
import com.pangratz.memorablepw.model.Password;

public class TweetPasswordServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(TweetPasswordServlet.class.getName());
	private ModelUtils mModelUtils;
	private Twitter mTwitter;

	@Override
	public void init() throws ServletException {
		super.init();

		mModelUtils = ModelUtils.getInstance();
		mTwitter = TwitterFactory.getSingleton();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int length = mModelUtils.getNextPasswordLength();
		Password pw = mModelUtils.getNextPassword(length);
		if (pw != null) {
			try {
				String tanga = pw.getText();
				mTwitter.updateStatus(tanga);
				mModelUtils.removePassword(tanga);
				mModelUtils.updateNextPasswordLength();
			} catch (TwitterException e) {
				log.log(Level.SEVERE, e.getErrorMessage(), e);
			}
		} else {
			log.log(Level.SEVERE, "no more passwords available :( !! feed me!");
		}
	}
}
