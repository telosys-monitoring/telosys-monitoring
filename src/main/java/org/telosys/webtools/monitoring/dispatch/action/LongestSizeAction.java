package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Number of longest requests
 */
public class LongestSizeAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		final int longestSizeNew = utils.parseInt( params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE), data.longestSize );
		if((longestSizeNew > 0) && (longestSizeNew != data.longestSize)) {
			data.longestSize = longestSizeNew;
			if(data.longestRequests != null) {
				data.longestRequests = new TopRequests(data.longestRequests, data.longestSize);
			}
		}
		if(data.longestRequests == null) {
			data.longestRequests = new TopRequests(data.longestSize);
		}
	}

}
