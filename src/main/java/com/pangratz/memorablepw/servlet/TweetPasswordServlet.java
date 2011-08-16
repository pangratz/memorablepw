package com.pangratz.memorablepw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pangratz.memorablepw.model.ModelUtils;
import com.pangratz.memorablepw.model.Password;

public class TweetPasswordServlet extends HttpServlet {

	private ModelUtils mModelUtils;

	@Override
	public void init() throws ServletException {
		super.init();

		mModelUtils = ModelUtils.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Password pw = mModelUtils.getNextPassword(0, "en");
		if (pw != null)
			log("tweet password: " + pw.getText());
		resp.flushBuffer();
	}
}
