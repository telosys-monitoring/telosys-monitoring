package org.telosys.webtools.monitoring.dispatch.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Clean all logs : url?action=clear
 */
public class URLParamsFilterAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_URL_PARAMS_FILTER) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		final List<String> urlParamsFilter = utils.parseArrayOfString( params.get(MonitorAttributeNames.ATTRIBUTE_NAME_URL_PARAMS_FILTER), new ArrayList<String>(), ',' );
		data.urlParamsFilter = urlParamsFilter;
	}

}
