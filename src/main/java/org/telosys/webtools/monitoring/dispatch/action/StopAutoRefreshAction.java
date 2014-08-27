package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Clean all logs : url?action=clear
 */
public class StopAutoRefreshAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return MonitorAttributeNames.ATTRIBUTE_VALUE_AUTO_REFRESH_STOP.equals(params.get(MonitorAttributeNames.ATTRIBUTE_NAME_AUTO_REFRESH));
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		data.autoRefreshActivated = false;
	}

}
