package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Clean all logs : url?action=clear
 */
public class ByTimeSizeAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		final int topTenSizeNew = utils.parseInt( params.get(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE), data.topTenSize );
		if((topTenSizeNew > 0) && (topTenSizeNew != data.topTenSize)) {
			data.topTenSize = topTenSizeNew;
			if(data.topRequests != null) {
				data.topRequests = new TopRequests(data.topRequests, data.topTenSize);
			}
		}
		if(data.topRequests == null) {
			data.topRequests = new TopRequests(data.topTenSize);
		}
	}

}
