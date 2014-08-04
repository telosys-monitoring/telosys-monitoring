package org.telosys.webtools.monitoring.dispatch.reporting;

import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.monitor.MonitorData;

/**
 * Monitor reporting.
 */
public interface Reporting {
	
	/**
	 * Displays monitor reporting.
	 * @param response HTTP response
	 * @param data Monitor data
	 */
	public void reporting(HttpServletResponse response, MonitorData data);
	
}
