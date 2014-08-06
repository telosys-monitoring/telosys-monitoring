package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Clean all logs : url?action=clear
 */
public class DurationThresholdAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		data.durationThreshold =
				utils.parseInt(
						params.get(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD),
						data.durationThreshold);
	}

}
