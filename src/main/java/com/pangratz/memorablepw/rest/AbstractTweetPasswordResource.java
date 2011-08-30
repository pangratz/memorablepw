package com.pangratz.memorablepw.rest;

import java.io.IOException;
import java.util.Properties;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.pangratz.memorablepw.model.Password;

public abstract class AbstractTweetPasswordResource extends MemorablePwServerResource {

	public static final String STATUS_CODE_DUPLICATE = "b2b52c28-1c67fec4";
	public static final String KEY_TOKEN = "token";
	public static final String KEY_SECRET = "secret";

	private Twitter mTwitter;
	private String mLang;

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();

		mLang = getLang();

		String propertiesName = String.format("/memorablepw_%s.properties", mLang);
		Properties twitterProps = new Properties();
		try {
			twitterProps.load(AbstractTweetPasswordResource.class.getResourceAsStream(propertiesName));
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
		}
		String token = twitterProps.getProperty(KEY_TOKEN);
		String secret = twitterProps.getProperty(KEY_SECRET);
		AccessToken accessToken = new AccessToken(token, secret);
		mTwitter = new TwitterFactory().getInstance(accessToken);

		getVariants(Method.GET).add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		int length = mModelUtils.getNextPasswordLength(mLang);
		Password pw = mModelUtils.getNextPassword(length, mLang);
		if (pw != null) {
			String tanga = pw.getText();
			try {
				mTwitter.updateStatus(tanga);
				mModelUtils.removePassword(tanga);
				mModelUtils.updateNextPasswordLength(mLang);

				return createSuccessRepresentation("tweeted password: " + tanga);
			} catch (TwitterException e) {
				// delete the password, if the status is a duplicate
				if (STATUS_CODE_DUPLICATE.equalsIgnoreCase(e.getExceptionCode())) {
					mModelUtils.removePassword(tanga);
				}
				return createErrorRepresentation("could not tweet password " + tanga + " --> " + e.getErrorMessage());
			}
		} else {
			return createErrorRepresentation("no password available! feed me :(");
		}
	}

	protected abstract String getLang();

}
