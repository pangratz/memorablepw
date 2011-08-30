package com.pangratz.memorablepw.rest;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import com.pangratz.memorablepw.model.ModelUtils;

public class UpdatePasswordCountersResource extends MemorablePwServerResource {

	private ModelUtils mModelUtils;

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();

		mModelUtils = ModelUtils.getInstance();
	}

	@Override
	protected Representation get() throws ResourceException {
		mModelUtils.updatePasswordCounters();
		return createSuccessRepresentation("updated password counters");
	}

}
