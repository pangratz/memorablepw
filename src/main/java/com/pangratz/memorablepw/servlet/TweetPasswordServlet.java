package com.pangratz.memorablepw.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
		PrintWriter writer = resp.getWriter();
		Password pw = mModelUtils.getNextPassword(0, "en");
		if (pw != null) {
			writer.write("tweet password: " + pw.getText());
		} else {
			writer.write("no password available for lang en");
		}
		writer.flush();
		writer.close();
	}
}
