package com.pangratz.memorablepw.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pangratz.memorablepw.model.ModelUtils;
import com.pangratz.memorablepw.model.Password;

public class TweetPasswordServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(TweetPasswordServlet.class.getName());
	private ModelUtils mModelUtils;

	@Override
	public void init() throws ServletException {
		super.init();

		mModelUtils = ModelUtils.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Password pw = mModelUtils.getNextPassword(0, "en");
		if (pw != null) {
			log.info("tweet password: " + pw.getText());
		} else {
			log.info("no password available for lang en");
		}
		resp.flushBuffer();
	}
}
