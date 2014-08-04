package org.telosys.webtools.monitoring.dispatch.action;

import java.util.Map;

import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.monitor.InitValues;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorWebXmlManager;
import org.telosys.webtools.monitoring.monitor.RequestAttributeNames;
import org.telosys.webtools.monitoring.monitor.Utils;

/**
 * Make actions.
 */
public class Action {

	/**
	 * Web.xml manager.
	 */
	protected MonitorWebXmlManager monitorWebXmlManager = new MonitorWebXmlManager();

	/**
	 * Utils.
	 */
	protected Utils utils = new Utils();

	/**
	 * Actions on monitoring.
	 * @param params Parameters
	 * @param data Monitor data
	 * @param initValues Init values from web.xml
	 */
	public boolean action(final Map<String,String> params, final MonitorData data, final InitValues initValues) {

		boolean isMakingAction = false;

		//--- Parameter : clean all logs
		if(params.get(RequestAttributeNames.ATTRIBUTE_NAME_ACTION) != null) {
			if(RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_CLEAR.equals(params.get(RequestAttributeNames.ATTRIBUTE_NAME_ACTION))) {
				isMakingAction = true;
				data.logLines = new CircularStack(data.logSize);
				data.topRequests = new TopRequests(data.topTenSize);
				data.longestRequests = new LongestRequests(data.longestSize);
			}
			if(RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_RESET.equals(params.get(RequestAttributeNames.ATTRIBUTE_NAME_ACTION))) {
				isMakingAction = true;
				monitorWebXmlManager.reset(initValues, data);
			}
			if(RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_STOP.equals(params.get(RequestAttributeNames.ATTRIBUTE_NAME_ACTION))) {
				isMakingAction = true;
				data.activated = false;
			}
			if(RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_START.equals(params.get(RequestAttributeNames.ATTRIBUTE_NAME_ACTION))) {
				isMakingAction = true;
				data.activated = true;
			}
		}

		//--- Parameter : request duration threshold
		if(params.get(RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD) != null) {
			isMakingAction = true;
			data.durationThreshold =
					utils.parseInt(
							params.get(RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD),
							data.durationThreshold);
		}

		//--- Parameter : memory log size
		if(params.get(RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE) != null) {
			isMakingAction = true;
			final int logSizeNew = utils.parseInt( params.get(RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE), data.logSize );
			if((logSizeNew > 0) && (logSizeNew != data.logSize)) {
				data.logSize = logSizeNew;
				data.logLines = new CircularStack(data.logLines, data.logSize);
			}
		}

		//--- Parameter : memory top ten size
		if(params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE) != null) {
			isMakingAction = true;
			final int topTenSizeNew = utils.parseInt( params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE), data.topTenSize );
			if((topTenSizeNew > 0) && (topTenSizeNew != data.topTenSize)) {
				data.topTenSize = topTenSizeNew;
				data.topRequests = new TopRequests(data.topRequests, data.topTenSize);
			}
		}

		//--- Parameter : memory longest requests size
		if(params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE) != null) {
			isMakingAction = true;
			final int longestSizeNew = utils.parseInt( params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE), data.longestSize );
			if((longestSizeNew > 0) && (longestSizeNew != data.longestSize)) {
				data.longestSize = longestSizeNew;
				data.longestRequests = new LongestRequests(data.longestRequests, data.longestSize);
			}
		}

		//--- Parameter : trace
		if(params.get(RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG) != null) {
			isMakingAction = true;
			final String traceParam = params.get(RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG);
			data.traceFlag = "true".equalsIgnoreCase(traceParam);
		}

		return isMakingAction;
	}

	/**
	 * @return the monitorWebXmlManager
	 */
	public MonitorWebXmlManager getMonitorWebXmlManager() {
		return monitorWebXmlManager;
	}

	/**
	 * @param monitorWebXmlManager the monitorWebXmlManager to set
	 */
	public void setMonitorWebXmlManager(final MonitorWebXmlManager monitorWebXmlManager) {
		this.monitorWebXmlManager = monitorWebXmlManager;
	}

	/**
	 * @return the utils
	 */
	public Utils getUtils() {
		return utils;
	}

	/**
	 * @param utils the utils to set
	 */
	public void setUtils(final Utils utils) {
		this.utils = utils;
	}
}
