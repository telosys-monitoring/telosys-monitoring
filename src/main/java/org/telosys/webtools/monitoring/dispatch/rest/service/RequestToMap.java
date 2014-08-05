package org.telosys.webtools.monitoring.dispatch.rest.service;

import java.util.HashMap;
import java.util.Map;

import org.telosys.webtools.monitoring.bean.Request;

/**
 * Transform Request to Map.
 */
public class RequestToMap {

	/**
	 * Transform request data to map.
	 * @param request Request
	 * @return map
	 */
	public Map<String, Object> transformRequestToMap(final Request request) {
		final Map<String, Object> map = new HashMap<String, Object>();

		map.put("countAllRequest", request.countAllRequest);
		map.put("countLongTimeRequests", request.countLongTimeRequests);
		map.put("elapsedTime", request.elapsedTime);
		map.put("url", request.getURL());
		map.put("startTime", request.startTime);

		return map;
	}

}
