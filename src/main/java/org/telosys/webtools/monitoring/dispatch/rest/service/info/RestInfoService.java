package org.telosys.webtools.monitoring.dispatch.rest.service.info;

import java.util.Map;

import org.telosys.webtools.monitoring.dispatch.rest.service.AbstractRestService;
import org.telosys.webtools.monitoring.dispatch.rest.service.RestService;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.util.JSONWriter;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * REST URL : /rest/info
 */
public class RestInfoService extends AbstractRestService implements RestService {

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
	public boolean match(final String[] paths, final Map<String,String> params) {
		if(paths == null) {
			return false;
		}
		return (paths.length == 1) && "info".equals(paths[0]);
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

		monitoring.put("initializationDate", data.initializationDate);
		monitoring.put("countAllRequest", data.countAllRequest);
		monitoring.put("countLongTimeRequests", data.countLongTimeRequests);

		return json;
	}

}
