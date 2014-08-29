package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Clean all logs : url?action=clear
 */
public class CleanAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_CLEAR.equals(params.get(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION));
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		data.latestLines = new CircularStack(data.latestSize);
		data.longestRequests = new TopRequests(data.longestSize);
		data.longestByUrlTempRequests = new LongestRequests(data.longestByUrlTempSize);
	}

}
