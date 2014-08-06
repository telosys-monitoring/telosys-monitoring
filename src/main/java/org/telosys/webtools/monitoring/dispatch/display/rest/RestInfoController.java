package org.telosys.webtools.monitoring.dispatch.display.rest;

import java.util.Map;

import org.telosys.webtools.monitoring.dispatch.display.Controller;
import org.telosys.webtools.monitoring.dispatch.display.rest.base.AbstractRestController;
import org.telosys.webtools.monitoring.monitor.MonitorData;

/**
 * REST URL : /rest/info
 */
public class RestInfoController extends AbstractRestController implements Controller {

	/**
	 * Indicates if URL paths match to this manager.
	 * @param paths URL paths
	 * @return boolean
	 */
	public boolean match(final String[] paths, final Map<String,String> params) {
		if(paths == null) {
			return false;
		}
		if((paths.length < 2) || (paths.length > 2)) {
			return false;
		}
		return "rest".equals(paths[0]) && "info".equals(paths[1]);
	}

	@Override
	public Map<String, Object> getData(final String[] paths, final Map<String, String> params, final MonitorData data) {
		final Map<String, Object> json = newMap();

		final Map<String, Object> host = newMap();
		json.put("host", host);

		host.put("ipAdress", data.ipAddress);
		host.put("hostname", data.hostname);
		host.put("java.version", System.getProperty("java.version"));
		host.put("java.vendor", System.getProperty("java.vendor"));
		host.put("os.arch", System.getProperty("os.arch"));
		host.put("os.name", System.getProperty("os.name"));
		host.put("os.version", System.getProperty("os.version"));

		final Map<String, Object> configuration = newMap();
		json.put("configuration", configuration);

		configuration.put("durationThreshold", data.durationThreshold);
		configuration.put("logSize", data.logSize);
		configuration.put("topTenSize", data.topTenSize);
		configuration.put("longestSize", data.longestSize);

		final Map<String, Object> monitoring = newMap();
		json.put("monitoring", monitoring);

		monitoring.put("activated", data.activated);
		monitoring.put("initializationDate", data.initializationDate);
		monitoring.put("countAllRequest", data.countAllRequest);
		monitoring.put("countLongTimeRequests", data.countLongTimeRequests);

		return json;
	}

}
