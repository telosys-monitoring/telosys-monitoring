package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.monitor.MonitorInitValuesManager;

/**
 * Clean all logs : url?action=clear
 */
public class ResetAction implements Action {

	protected MonitorInitValuesManager monitorInitValuesManager = new MonitorInitValuesManager();

	public boolean match(final String[] paths, final Map<String, String> params) {
		return MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_RESET.equals(params.get(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION));
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		monitorInitValuesManager.reset(initValues, data);
	}

	public MonitorInitValuesManager getMonitorInitValuesManager() {
		return monitorInitValuesManager;
	}

	public void setMonitorInitValuesManager(
			final MonitorInitValuesManager monitorInitValuesManager) {
		this.monitorInitValuesManager = monitorInitValuesManager;
	}

}
