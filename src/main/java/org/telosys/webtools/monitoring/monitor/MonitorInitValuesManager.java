package org.telosys.webtools.monitoring.monitor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.util.Utils;

public class MonitorInitValuesManager {

	/**
	 * Utils.
	 */
	private Utils utils = new Utils();

	/**
	 * Save init values from web.xml configuration.
	 * @param filterConfig Filter configuration
	 * @throws ServletException Error
	 */
	public MonitorInitValues initValues(final FilterConfig filterConfig) {
		final MonitorInitValues initValues = new MonitorInitValues();

		//--- Parameter : activated
		final String activatedParam = filterConfig.getInitParameter("activated");
		if ( activatedParam != null ) {
			initValues.activated = activatedParam.equalsIgnoreCase("true");
		}

		//--- Parameter : parameters
		initValues.urlParamsActivated =
				utils.parseBoolean(
						filterConfig.getInitParameter(MonitorAttributeNames.ATTRIBUTE_NAME_URL_PARAMS_ACTIVATED),
						MonitorInitValues.DEFAULT_URL_PARAMS_ACTIVATED );

		//--- Parameter : parameters names
		final List<String> urlParamsFiltersDefault = new ArrayList<String>();
		urlParamsFiltersDefault.add("*");
		initValues.urlParamsFilter =
				utils.parseArrayOfString(
						filterConfig.getInitParameter(MonitorAttributeNames.ATTRIBUTE_NAME_URL_PARAMS_FILTER),
						urlParamsFiltersDefault,
						',');

		//--- Parameter : parameters empty
		initValues.urlParamsEmpty =
				utils.parseBoolean(
						filterConfig.getInitParameter(MonitorAttributeNames.ATTRIBUTE_NAME_URL_PARAMS_EMPTY),
						MonitorInitValues.DEFAULT_URL_PARAMS_EMPTY );

		//--- Parameter : duration threshold
		initValues.durationThreshold =
				utils.parseInt(
						filterConfig.getInitParameter(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD),
						MonitorInitValues.DEFAULT_DURATION_THRESHOLD );

		//--- Parameter : memory log size
		initValues.latestSize =
				utils.parseInt(
						filterConfig.getInitParameter(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE),
						MonitorInitValues.DEFAULT_LATEST_SIZE );

		//--- Parameter : memory longest requests size
		initValues.longestSize =
				utils.parseInt(
						filterConfig.getInitParameter(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE),
						MonitorInitValues.DEFAULT_LONGEST_SIZE );

		//--- Parameter : memory longest requests by url size
		initValues.longestByUrlSize =
				utils.parseInt(
						filterConfig.getInitParameter(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE),
						MonitorInitValues.DEFAULT_LONGEST_BY_URL_SIZE );

		//--- Parameter : status report URI
		final String reportingParam = filterConfig.getInitParameter("reporting");
		if ( reportingParam != null ) {
			initValues.reportingReqPath = reportingParam;
		}

		//--- Parameter : trace
		final String traceParam = filterConfig.getInitParameter("trace");
		if ( traceParam != null ) {
			initValues.traceFlag = traceParam.equalsIgnoreCase("true");
		}

		return initValues;
	}

	/**
	 * Reinitialize parameters values.
	 * @param initValues Initial values
	 * @param monitorBean Data monitor
	 */
	public void reset(final MonitorInitValues initValues, final MonitorData monitorBean) {

		//--- Parameter : duration threshold
		monitorBean.durationThreshold = initValues.durationThreshold;

		//--- Parameter : activated
		monitorBean.activated = initValues.activated;

		//--- Parameter : memory log size
		monitorBean.latestSize = initValues.latestSize;
		monitorBean.latestLines = new CircularStack(monitorBean.latestSize);

		//--- Parameter : memory top ten size
		monitorBean.longestSize = initValues.longestByUrlSize;
		monitorBean.longestRequests = new TopRequests(monitorBean.longestSize);

		//--- Parameter : memory longest requests size
		monitorBean.longestByUrlTempSize = initValues.longestSize;
		monitorBean.longestByUrlTempRequests = new LongestRequests(monitorBean.longestByUrlTempSize);

		//--- Parameter : status report URI
		monitorBean.reportingReqPath = initValues.reportingReqPath;

		//--- Parameter : trace
		monitorBean.traceFlag = initValues.traceFlag;

		//--- Parameter : url params
		monitorBean.urlParamsActivated = initValues.urlParamsActivated;
		monitorBean.urlParamsFilter = initValues.urlParamsFilter;
		monitorBean.urlParamsEmpty = initValues.urlParamsEmpty;

		monitorBean.initializationDate = utils.format( new Date() );

		final InetAddress adrLocale = utils.getLocalHost();
		if(adrLocale == null) {
			monitorBean.ipAddress = "unknown";
			monitorBean.hostname = "unknwon";
		} else {
			monitorBean.ipAddress = adrLocale.getHostAddress();
			monitorBean.hostname = adrLocale.getHostName();
		}
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(final Utils utils) {
		this.utils = utils;
	}

}
