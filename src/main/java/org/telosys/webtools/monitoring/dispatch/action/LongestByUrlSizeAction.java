package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Number of longest requests by URL
 */
public class LongestByUrlSizeAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		final int longestByUrlTempSizeNew = utils.parseInt( params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE), data.longestByUrlTempSize );
		if((longestByUrlTempSizeNew > 0) && (longestByUrlTempSizeNew != data.longestByUrlTempSize)) {
			data.longestByUrlTempSize = longestByUrlTempSizeNew;
			if(data.longestByUrlTempRequests != null) {
				data.longestByUrlTempRequests = new LongestRequests(data.longestByUrlTempRequests, data.longestByUrlTempSize);
			}
		}
		if(data.longestByUrlTempRequests == null) {
			data.longestByUrlTempRequests = new LongestRequests(data.longestByUrlTempSize);
		}
	}

}
