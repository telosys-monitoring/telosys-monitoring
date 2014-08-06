package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Make actions.
 */
public interface Action {

	/**
	 * Indicates if URL paths match to this manager.
	 * @param paths URL paths
	 * @param params URL parameters
	 * @return boolean
	 */
	boolean match(final String[] paths, Map<String, String> params);

	/**
	 * Actions on monitoring.
	 * @param params Parameters
	 * @param data Monitor data
	 * @param initValues Init values from web.xml
	 */
	void action(final Map<String,String> params, final MonitorData data, final MonitorInitValues initValues);

}
