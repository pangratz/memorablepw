package com.pangratz.memorablepw.rest;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class RemovePasswordResource extends MemorablePwServerResource {

	private String mPassword;

	@Override
	protected Representation delete() throws ResourceException {
		try {
			mModelUtils.removePassword(this.mPassword);
			return createSuccessRepresentation("removed password");
		} catch (Exception e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL);
		}

		return createErrorRepresentation("error while removing password");
	}

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();

		getVariants(Method.DELETE).add(new Variant(MediaType.APPLICATION_JSON));

		this.mPassword = (String) getRequest().getAttributes().get("password");
	}

}
