package com.pangratz.memorablepw.rest;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class AddPasswordsResource extends MemorablePwServerResource {

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();

		getVariants(Method.POST).add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation post(Representation entity, Variant variant) throws ResourceException {
		Representation result = null;
		if (entity != null) {
			if (MediaType.APPLICATION_JSON.equals(entity.getMediaType(), true)) {
				JsonRepresentation represent;
				try {
					represent = new JsonRepresentation(entity);
					JSONObject json = represent.getJsonObject();

					System.out.println(json);

					return createSuccessRepresentation("created passwords");
				} catch (Exception e) {
					e.printStackTrace();
					setStatus(Status.SERVER_ERROR_INTERNAL);
				}

				return createErrorRepresentation("error while creating NetworkPlanEntry");
			}
		}
		return result;
	}

}
