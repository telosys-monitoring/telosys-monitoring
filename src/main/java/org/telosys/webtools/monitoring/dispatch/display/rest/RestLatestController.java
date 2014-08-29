package org.telosys.webtools.monitoring.dispatch.display.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.display.Controller;
import org.telosys.webtools.monitoring.dispatch.display.rest.base.AbstractRestController;
import org.telosys.webtools.monitoring.monitor.MonitorData;

/**
 * REST URL : /rest/log
 */
public class RestLatestController extends AbstractRestController implements Controller {

	/**
	 * Indicates if URL paths match to this manager.
	 * @param paths URL paths
	 * @return boolean
	 */
	public boolean match(final String[] paths, final Map<String, String> params) {
		if(paths == null) {
			return false;
		}
		if(paths.length != 2) {
			return false;
		}
		return "rest".equals(paths[0]) && "latest".equals(paths[1]);
	}

	@Override
	public Map<String, Object> getData(final String[] paths, final Map<String,String> params, final MonitorData data) {
		final Map<String, Object> json = newMap();

		final List<Map<String,Object>> latest = new ArrayList<Map<String,Object>>();
		json.put("latest", latest);

		final List<Request> requests = getRequests(paths, params, data);

		for(final Request request : requests) {
			latest.add(getRequestToMap().transformRequestToMap(request));
		}

		return json;
	}

	/**
	 * Get requests
	 * @param data Monitor data
	 * @return requests
	 */
	protected List<Request> getRequests(final String[] paths, final Map<String, String> params, final MonitorData data) {
		final List<Request> requests;

		final String startAsString = utils.trimToNull(params.get("start"));
		if(startAsString != null) {
			requests = new ArrayList<Request>();
			final Integer start = utils.parseInt(startAsString, 0);
			for(final Request request : data.latestLines.getAllAscending()) {
				if(request.countLongTimeRequests >= start) {
					requests.add(request);
				}
			}
		} else {
			requests = data.latestLines.getAllAscending();
		}

		return requests;
	}

}
