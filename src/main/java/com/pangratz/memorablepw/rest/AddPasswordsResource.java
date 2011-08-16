package com.pangratz.memorablepw.rest;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import com.pangratz.memorablepw.model.Password;

public class AddPasswordsResource extends MemorablePwServerResource {

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();

		getVariants(Method.POST).add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation post(Representation entity, Variant variant) throws ResourceException {
		if (entity != null) {
			try {
				JsonRepresentation represent = new JsonRepresentation(entity);
				JSONArray json = represent.getJsonArray();

				// iterate over each password entry
				List<Password> passwords = new LinkedList<Password>();
				int length = json.length();
				for (int i = 0; i < length; i++) {
					JSONObject object = (JSONObject) json.get(i);
					String lang = object.has("lang") ? object.getString("lang") : "en";
					String pw = object.getString("password");
					System.out.println(pw + " --> " + lang);
					passwords.add(new Password(pw, lang));
				}

				// add passwords to model
				mModelUtils.addPasswords(passwords);

				return createSuccessRepresentation("created passwords");
			} catch (Exception e) {
				e.printStackTrace();
				setStatus(Status.SERVER_ERROR_INTERNAL);
			}

			return createErrorRepresentation("error while creating passwords");
		}
		return createErrorRepresentation("no passwords in body");
	}
}
