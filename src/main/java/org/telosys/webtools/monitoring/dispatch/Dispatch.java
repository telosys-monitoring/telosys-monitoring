package org.telosys.webtools.monitoring.dispatch;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.dispatch.action.Action;
import org.telosys.webtools.monitoring.dispatch.parameter.GetParameters;
import org.telosys.webtools.monitoring.dispatch.reporting.Reporting;
import org.telosys.webtools.monitoring.dispatch.reporting.html.HtmlReporting;
import org.telosys.webtools.monitoring.dispatch.rest.RestManager;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.util.Log;

public class Dispatch {

	/** Get URL parameters */
	protected GetParameters getParameters = new GetParameters();

	/** Make actions */
	protected Action action = new Action();

	/** Make actions */
	protected Reporting reporting = new HtmlReporting();

	/** REST */
	protected RestManager restManager = new RestManager();

	/** Log */
	protected Log log = new Log();

	/**
	 * Command for reporting.
	 * @param httpServletRequest request
	 * @param httpServletResponse response
	 * @param data monitor data
	 * @param initValues Init values from web.xml
	 */
	public void dispatch(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final MonitorData data, final MonitorInitValues initValues) {
		final Map<String,String> params = getParameters.getParameters(httpServletRequest);
		final boolean isMakingAction = action.action(params, data, initValues);
		if(isMakingAction) {
			// Redirection to the default reporting url
			final String redirectURL = httpServletRequest.getRequestURL().toString();
			try {
				httpServletResponse.sendRedirect(redirectURL);
			} catch (final IOException e) {
				log.manageError(e);
			}
		} else {
			final boolean isRestURL = restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues);
			if(isRestURL) {
				// Rest URL
				restManager.process(httpServletRequest, httpServletResponse, data, initValues);
			} else {
				// Report page
				reporting.reporting(httpServletResponse, data);
			}
		}
	}

	/**
	 * @return the getParameters
	 */
	public GetParameters getGetParameters() {
		return getParameters;
	}

	/**
	 * @param getParameters the getParameters to set
	 */
	public void setGetParameters(final GetParameters getParameters) {
		this.getParameters = getParameters;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(final Action action) {
		this.action = action;
	}

	/**
	 * @return the reporting
	 */
	public Reporting getReporting() {
		return reporting;
	}

	/**
	 * @param reporting the reporting to set
	 */
	public void setReporting(final Reporting reporting) {
		this.reporting = reporting;
	}

	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(final Log log) {
		this.log = log;
	}

}
