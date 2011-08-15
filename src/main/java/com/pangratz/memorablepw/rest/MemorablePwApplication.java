package com.pangratz.memorablepw.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class MemorablePwApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/password/length/{length}", RetrievePasswordResource.class);
		router.attach("/password/{password}", RemovePasswordResource.class);
		router.attach("/password", AddPasswordsResource.class);
		return router;
	}
}
