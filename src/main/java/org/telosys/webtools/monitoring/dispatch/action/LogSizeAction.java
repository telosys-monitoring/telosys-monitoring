package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Clean all logs : url?action=clear
 */
public class LogSizeAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		final int logSizeNew = utils.parseInt( params.get(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE), data.logSize );
		if((logSizeNew > 0) && (logSizeNew != data.logSize)) {
			data.logSize = logSizeNew;
			if(data.logLines != null) {
				data.logLines = new CircularStack(data.logLines, data.logSize);
			}
		}
		if(data.logLines == null) {
			data.logLines = new CircularStack(data.logSize);
		}
	}

}
