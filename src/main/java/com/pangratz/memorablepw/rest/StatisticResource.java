package com.pangratz.memorablepw.rest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import com.pangratz.memorablepw.model.Statistic;

public class StatisticResource extends MemorablePwServerResource {

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();

		getVariants(Method.GET).add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Statistic statistic = mModelUtils.getStatistic();
		List<Object> data = new LinkedList<Object>();
		for (int i = 8; i <= 31; i++) {
			int count = statistic.getPasswordsCount(i);
			Map<Object, Object> statData = new HashMap<Object, Object>();
			statData.put("length", i);
			statData.put("count", count);
			JSONObject obj = new JSONObject(statData);
			data.add(obj);
		}

		Map<Object, Object> objData = new HashMap<Object, Object>();
		objData.put("passwords", new JSONArray(data));
		objData.put("overallPasswordsCount", statistic.getOverallPasswordsCount());

		return new JsonRepresentation(new JSONObject(objData));
	}
}
