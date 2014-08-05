package org.telosys.webtools.monitoring.dispatch.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Process one REST URL.
 */
public interface RestService {

	/**
	 * Process Rest URL : /rest/info
	 * @param httpServletRequest Request
	 * @param httpServletResponse Response
	 * @param data Monitor data
	 * @param initValues Init values
	 */
	void process(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final MonitorData data,
			final MonitorInitValues initValues);

	/**
	 * Indicates if URL paths match to this manager.
	 * @param paths URL paths
	 * @return boolean
	 */
	boolean match(final String[] paths);

}
