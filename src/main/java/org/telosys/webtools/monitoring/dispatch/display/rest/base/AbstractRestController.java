package org.telosys.webtools.monitoring.dispatch.display.rest.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.dispatch.display.Controller;
import org.telosys.webtools.monitoring.dispatch.display.rest.common.RequestToMap;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.util.JSONWriter;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * REST URL : /rest/info
 */
public abstract class AbstractRestController implements Controller {

	/**
	 * JSON writer.
	 */
	private JSONWriter jsonWriter = new JSONWriter();

	/**
	 * Utils.
	 */
	protected Utils utils = new Utils();

	/**
	 * Request to Map.
	 */
	private RequestToMap requestToMap = new RequestToMap();

	/**
	 * Process Rest URL : /rest/info
	 * @param httpServletRequest Request
	 * @param httpServletResponse Response
	 * @param data Monitor data
	 * @param initValues Init values
	 */
	public void process(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse,
			final String[] paths,
			final Map<String, String> params,
			final MonitorData data,
			final MonitorInitValues initValues) {
		httpServletResponse.setContentType("application/json");

		//--- Prevent caching
		httpServletResponse.setHeader("Pragma", "no-cache"); // Set standard HTTP/1.0 no-cache header.
		httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // Set standard HTTP/1.1 no-cache header.
		httpServletResponse.setDateHeader ("Expires", 0); // Prevents caching on proxies

		final Map<String, Object> map = getData(paths, params, data);
		final StringBuffer str = new StringBuffer();
		jsonWriter.write(str, map);

		PrintWriter out;
		try {
			out = httpServletResponse.getWriter();
			out.print(str.toString());
			out.close();
		} catch (final IOException e) {
			throw new RuntimeException("RequestMonitor error : cannot get writer");
		}
	}

	/**
	 * Return data to add in JSON.
	 * @param paths URL paths
	 * @param params URL params
	 * @param data Monitor Data
	 * @return JSON content
	 */
	public abstract Map<String, Object> getData(final String[] paths, final Map<String,String> params, final MonitorData data);

	/**
	 * Get new Map.
	 * @return new Map
	 */
	public Map<String, Object> newMap() {
		return new LinkedHashMap<String, Object>();
	}

	/**
	 * @return the jsonWriter
	 */
	public JSONWriter getJsonWriter() {
		return jsonWriter;
	}

	/**
	 * @param jsonWriter the jsonWriter to set
	 */
	public void setJsonWriter(final JSONWriter jsonWriter) {
		this.jsonWriter = jsonWriter;
	}

	/**
	 * @return the utils
	 */
	public Utils getUtils() {
		return utils;
	}

	/**
	 * @param utils the utils to set
	 */
	public void setUtils(final Utils utils) {
		this.utils = utils;
	}

	/**
	 * @return the requestToMap
	 */
	public RequestToMap getRequestToMap() {
		return requestToMap;
	}

	/**
	 * @param requestToMap the requestToMap to set
	 */
	public void setRequestToMap(final RequestToMap requestToMap) {
		this.requestToMap = requestToMap;
	}

}
