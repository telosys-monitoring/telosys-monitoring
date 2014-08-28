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

		host.put("ip_adress", data.ipAddress);
		host.put("hostname", data.hostname);
		host.put("java_version", System.getProperty("java.version"));
		host.put("java_vendor", System.getProperty("java.vendor"));
		host.put("os_arch", System.getProperty("os.arch"));
		host.put("os_name", System.getProperty("os.name"));
		host.put("os_version", System.getProperty("os.version"));

		final Map<String, Object> configuration = newMap();
		json.put("configuration", configuration);

		configuration.put("duration", data.durationThreshold);
		configuration.put("log_size", data.logSize);
		configuration.put("by_time_size", data.topTenSize);
		configuration.put("by_url_size", data.longestSize);
		configuration.put("url_params_activated", (data.urlParamsActivated?"true":"false"));
		configuration.put("url_params_filter", utils.mergeToString(data.urlParamsFilter, ','));
		configuration.put("url_params_empty", (data.urlParamsEmpty?"true":"false"));

		final Map<String, Object> monitoring = newMap();
		json.put("monitoring", monitoring);

		monitoring.put("activated", data.activated);
		monitoring.put("initialization_date", data.initializationDate);
		monitoring.put("count_all_request", data.countAllRequest);
		monitoring.put("count_long_time_requests", data.countLongTimeRequests);

		return json;
	}

}
