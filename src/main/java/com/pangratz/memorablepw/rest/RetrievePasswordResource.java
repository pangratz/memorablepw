package com.pangratz.memorablepw.rest;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import com.pangratz.memorablepw.model.Password;

public class RetrievePasswordResource extends MemorablePwServerResource {

	private int mLength;

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();

		getVariants(Method.GET).add(new Variant(MediaType.APPLICATION_JSON));

		String tanga = (String) getRequest().getAttributes().get("length");
		this.mLength = Integer.parseInt(tanga);
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Password password = mModelUtils.getNextPassword(mLength);
		if (password == null) {
			return createErrorRepresentation("no password with given length available");
		}

		Map<Object, Object> data = new HashMap<Object, Object>();
		data.put("password", password.getPassword());
		data.put("length", password.getLength());
		return new JsonRepresentation(data);
	}
}
