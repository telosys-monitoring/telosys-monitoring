package org.telosys.webtools.monitoring.monitor;

import java.net.InetAddress;
import java.util.Date;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;

public class MonitorWebXmlManager {

	/**
	 * Utils.
	 */
	private Utils utils = new Utils();

	/**
	 * Save init values from web.xml configuration.
	 * @param filterConfig Filter configuration
	 * @throws ServletException Error
	 */
	public InitValues initValues(final FilterConfig filterConfig) {
		final InitValues initValues = new InitValues();

		//--- Parameter : activated
		final String activatedParam = filterConfig.getInitParameter("activated");
		if ( activatedParam != null ) {
			initValues.activated = activatedParam.equalsIgnoreCase("true");
		}

		//--- Parameter : duration threshold
		initValues.durationThreshold =
				utils.parseInt( filterConfig.getInitParameter("duration"), InitValues.DEFAULT_DURATION_THRESHOLD );

		//--- Parameter : memory log size
		initValues.logSize =
				utils.parseInt( filterConfig.getInitParameter("logsize"), InitValues.DEFAULT_LOG_SIZE );

		//--- Parameter : memory top ten size
		initValues.topTenSize =
				utils.parseInt( filterConfig.getInitParameter("toptensize"), InitValues.DEFAULT_TOP_TEN_SIZE );

		//--- Parameter : memory longest requests size
		initValues.longestSize =
				utils.parseInt( filterConfig.getInitParameter("longestsize"), InitValues.DEFAULT_LONGEST_SIZE );

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
	public void reset(final InitValues initValues, final MonitorData monitorBean) {

		//--- Parameter : duration threshold
		monitorBean.durationThreshold = initValues.durationThreshold;

		//--- Parameter : activated
		monitorBean.activated = initValues.activated;

		//--- Parameter : memory log size
		monitorBean.logSize = initValues.logSize;
		monitorBean.logLines = new CircularStack(monitorBean.logSize);

		//--- Parameter : memory top ten size
		monitorBean.topTenSize = initValues.topTenSize;
		monitorBean.topRequests = new TopRequests(monitorBean.topTenSize);

		//--- Parameter : memory longest requests size
		monitorBean.longestSize = initValues.longestSize;
		monitorBean.longestRequests = new LongestRequests(monitorBean.longestSize);

		//--- Parameter : status report URI
		monitorBean.reportingReqPath = initValues.reportingReqPath;

		//--- Parameter : trace
		monitorBean.traceFlag = initValues.traceFlag;

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
