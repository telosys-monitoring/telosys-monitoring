package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

/**
 * Activate storage of URL parameters : url?url_params_empty=true / false
 */
public class URLParamsEmptyAction extends AbstractAction implements Action {

	public boolean match(final String[] paths, final Map<String, String> params) {
		return params.get(MonitorAttributeNames.ATTRIBUTE_NAME_URL_PARAMS_EMPTY) != null;
	}

	public void action(final Map<String, String> params, final MonitorData data, final MonitorInitValues initValues) {
		final boolean urlParamsEmpty = utils.parseBoolean( params.get(MonitorAttributeNames.ATTRIBUTE_NAME_URL_PARAMS_EMPTY), data.urlParamsEmpty );
		data.urlParamsEmpty = urlParamsEmpty;
	}

}
