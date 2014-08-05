package org.telosys.webtools.monitoring.dispatch.rest.service.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.rest.service.AbstractRestService;
import org.telosys.webtools.monitoring.dispatch.rest.service.RestService;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.util.JSONWriter;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * REST URL : /rest/info
 */
public class RestLogService extends AbstractRestService implements RestService {

	/**
	 * JSON writer.
	 */
	private JSONWriter jsonWriter;

	/**
	 * Utils.
	 */
	protected Utils utils = new Utils();

	/**
	 * Indicates if URL paths match to this manager.
	 * @param paths URL paths
	 * @return boolean
	 */
	public boolean match(final String[] paths, final Map<String, String> params) {
		if(paths == null) {
			return false;
		}
		return ((paths.length == 1) || (paths.length == 2)) && "log".equals(paths[0]);
	}

	@Override
	public Map<String, Object> getData(final String[] paths, final Map<String,String> params, final MonitorData data) {
		final Map<String, Object> json = newMap();

		final List<String> log = new ArrayList<String>();
		json.put("log", log);

		final List<Request> requests = getRequests(paths, params, data);

		for(final Request request : requests) {
			log.add(request.toString());
		}

		return json;
	}

	/**
	 * Get requests
	 * @param data Monitor data
	 * @return requests
	 */
	protected List<Request> getRequests(final String[] paths, final Map<String, String> params, final MonitorData data) {
		final List<Request> requests = data.logLines.getAllAscending();

		return requests;
	}

}
