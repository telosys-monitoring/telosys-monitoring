package org.telosys.webtools.monitoring.dispatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.dispatch.action.Action;
import org.telosys.webtools.monitoring.dispatch.action.LongestSizeAction;
import org.telosys.webtools.monitoring.dispatch.action.LongestByUrlSizeAction;
import org.telosys.webtools.monitoring.dispatch.action.CleanAction;
import org.telosys.webtools.monitoring.dispatch.action.DurationThresholdAction;
import org.telosys.webtools.monitoring.dispatch.action.LatestAction;
import org.telosys.webtools.monitoring.dispatch.action.ResetAction;
import org.telosys.webtools.monitoring.dispatch.action.StartAction;
import org.telosys.webtools.monitoring.dispatch.action.StartAutoRefreshAction;
import org.telosys.webtools.monitoring.dispatch.action.StopAction;
import org.telosys.webtools.monitoring.dispatch.action.StopAutoRefreshAction;
import org.telosys.webtools.monitoring.dispatch.action.URLParamsActivatedAction;
import org.telosys.webtools.monitoring.dispatch.action.URLParamsEmptyAction;
import org.telosys.webtools.monitoring.dispatch.action.URLParamsFilterAction;
import org.telosys.webtools.monitoring.dispatch.display.Controller;
import org.telosys.webtools.monitoring.dispatch.display.rest.RestInfoController;
import org.telosys.webtools.monitoring.dispatch.display.rest.RestLatestController;
import org.telosys.webtools.monitoring.dispatch.display.rest.RestLongestController;
import org.telosys.webtools.monitoring.dispatch.display.rest.RestLongestByUrlRequestController;
import org.telosys.webtools.monitoring.dispatch.display.web.HtmlReporting;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.util.Log;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * Dispatch.
 */
public class Dispatch {

	/** Actions */
	private List<Action> actions = new ArrayList<Action>();

	/** Controllers */
	private List<Controller> controllers = new ArrayList<Controller>();

	/** Utils */
	private Utils utils = new Utils();

	/** Log */
	protected Log log = new Log();

	/**
	 * Constructor.
	 */
	public Dispatch() {

		// Actions
		actions.add(new CleanAction());
		actions.add(new ResetAction());
		actions.add(new StartAction());
		actions.add(new StopAction());
		actions.add(new DurationThresholdAction());
		actions.add(new LatestAction());
		actions.add(new LongestSizeAction());
		actions.add(new LongestByUrlSizeAction());
		actions.add(new URLParamsActivatedAction());
		actions.add(new URLParamsFilterAction());
		actions.add(new URLParamsEmptyAction());
		actions.add(new StartAutoRefreshAction());
		actions.add(new StopAutoRefreshAction());

		// Report HTML page
		controllers.add(new HtmlReporting());

		// REST URL
		controllers.add(new RestInfoController());
		controllers.add(new RestLatestController());
		controllers.add(new RestLongestByUrlRequestController());
		controllers.add(new RestLongestController());
	}

	/**
	 * Dispatch.
	 * @param httpServletRequest Request
	 * @param httpServletResponse Response
	 * @param data Monitor data
	 * @param initValues Init values
	 */
	public void dispatch(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse,
			final MonitorData data,
			final MonitorInitValues initValues) {

		// URL paths
		final String[] paths = getPaths(httpServletRequest, data);

		// URL parameters
		final Map<String, String> params = utils.getParameters(httpServletRequest);

		// Actions
		final boolean hasActions = doActions(httpServletRequest, httpServletResponse, paths, params, data, initValues);
		if(hasActions) {
			// Always redirect after actions
			redirect(httpServletRequest, httpServletResponse);
		} else {
			// Controller
			final Controller controller = getControllerForURLPaths(getControllers(), paths, params);
			if(controller == null) {
				// page not found
				httpServletResponse.setStatus(404);
			} else {
				// display page
				controller.process(httpServletRequest, httpServletResponse, paths, params, data, initValues);
			}
		}
	}

	/**
	 * Make actions.
	 * @param httpServletRequest request
	 * @param httpServletResponse response
	 * @param paths URL paths
	 * @param params URL params
	 * @param data Monitor data
	 * @param initValues Init values
	 * @return has actions
	 */
	public boolean doActions(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse,
			final String[] paths, final Map<String, String> params,
			final MonitorData data,
			final MonitorInitValues initValues) {

		boolean hasAction = false;

		final List<Action> actions = getActions(getActions(), paths, params);
		if(!actions.isEmpty()) {
			for(final Action action : actions) {
				if(action.match(paths, params)) {
					// do action
					action.action(params, data, initValues);

					hasAction = true;
				}
			}
		}
		return hasAction;
	}

	/**
	 * Return all actions to do.
	 * @param actions Actions
	 * @param paths URL paths
	 * @param params URL parameters
	 * @return actions
	 */
	private List<Action> getActions(final List<Action> actions, final String[] paths,
			final Map<String, String> params) {
		return actions;
	}

	/**
	 * Return the first Controller which matches to URL paths.
	 * @param controllers Rest services
	 * @param paths URL paths
	 * @return Controller
	 */
	protected Controller getControllerForURLPaths(final List<Controller> controllers, final String[] paths, final Map<String, String> params) {
		for(final Controller controller : controllers) {
			if(controller.match(paths, params)) {
				return controller;
			}
		}
		return null;
	}

	/**
	 * Redirect.
	 * @param httpServletRequest Request
	 * @param httpServletResponse Response
	 */
	private void redirect(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse) {
		// Redirection to the default reporting url
		final String redirectURL = httpServletRequest.getRequestURL().toString();
		try {
			httpServletResponse.sendRedirect(redirectURL);
		} catch (final IOException e) {
			log.manageError(e);
		}
	}

	/**
	 * Return paths from URL.
	 * @param httpServletRequest Request
	 * @param data Monitor data
	 * @return URL Paths
	 */
	public String[] getPaths(final HttpServletRequest httpServletRequest,
			final MonitorData data) {
		String url = utils.getURI(httpServletRequest);
		url = utils.removeRootURL(url, data.reportingReqPath);
		final String[] paths = utils.splitURLPaths(url);
		return paths;
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

	/**
	 * @return the actions
	 */
	public List<Action> getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(final List<Action> actions) {
		this.actions = actions;
	}

	/**
	 * @return the controllers
	 */
	public List<Controller> getControllers() {
		return controllers;
	}

	/**
	 * @param controllers the controllers to set
	 */
	public void setControllers(final List<Controller> controllers) {
		this.controllers = controllers;
	}


}
