package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Latest requests.
 */
public class LatestAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		final int latestSizeNew = utils.parseInt( params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE), data.latestSize );
		if((latestSizeNew > 0) && (latestSizeNew != data.latestSize)) {
			data.latestSize = latestSizeNew;
			if(data.latestLines != null) {
				data.latestLines = new CircularStack(data.latestLines, data.latestSize);
			}
		}
		if(data.latestLines == null) {
			data.latestLines = new CircularStack(data.latestSize);
		}
	}

}
