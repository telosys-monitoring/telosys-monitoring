package org.telosys.webtools.monitoring.dispatch.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.dispatch.parameter.GetParameters;
import org.telosys.webtools.monitoring.dispatch.rest.service.RestService;
import org.telosys.webtools.monitoring.dispatch.rest.service.info.RestInfoService;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * REST URL manager.
 */
public class RestManager {

	/**
	 * Utils.
	 */
	private Utils utils = new Utils();

	/**
	 * GetParameters.
	 */
	private GetParameters getParameters = new GetParameters();

	/**
	 * REST services.
	 */
	private List<RestService> restServices = new ArrayList<RestService>();

	/**
	 * REST base URL.
	 */
	public static final String REST_PATH_URL = "/rest";

	/**
	 * Constructor.
	 */
	public RestManager() {
		restServices.add(new RestInfoService());
	}

	/**
	 * Indicates if this URL is a REST URL.
	 * @param httpServletRequest Request
	 * @param httpServletResponse Response
	 * @param data Monitor data
	 * @param initValues Monitor init values
	 * @return is REST URL
	 */
	public boolean isRestURL(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final MonitorData data,
			final MonitorInitValues initValues) {

		if(utils.isBlank(data.reportingReqPath)) {
			return false;
		}
		final String pathInfo = httpServletRequest.getServletPath();
		if(pathInfo == null) {
			return false;
		}

		return pathInfo.startsWith(utils.joinURL(data.reportingReqPath, REST_PATH_URL));
	}

	/**
	 * Process Rest URLs : /rest/*
	 * @param httpServletRequest Request
	 * @param httpServletResponse Response
	 * @param data Monitor data
	 * @param initValues Init values
	 */
	public void process(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse,
			final MonitorData data,
			final MonitorInitValues initValues) {

		final String[] paths = getPaths(httpServletRequest, data);
		final Map<String, String> params = getParameters.getParameters(httpServletRequest);

		final RestService restService = getRestServiceForURLPaths(getRestServices(), paths, params);

		if(restService == null) {
			httpServletResponse.setStatus(404);
		} else {
			restService.process(httpServletRequest, httpServletResponse, paths, params, data, initValues);
		}
	}

	/**
	 * Return the first RestService which matches to URL paths.
	 * @param restServices Rest services
	 * @param paths URL paths
	 * @return RestService
	 */
	protected RestService getRestServiceForURLPaths(final List<RestService> restServices, final String[] paths, final Map<String, String> params) {
		for(final RestService restService : restServices) {
			if(restService.match(paths, params)) {
				return restService;
			}
		}
		return null;
	}

	/**
	 * Return paths from URL.
	 * @param httpServletRequest Request
	 * @param data Monitor data
	 * @return URL Paths
	 */
	public String[] getPaths(final HttpServletRequest httpServletRequest,
			final MonitorData data) {
		String url = httpServletRequest.getServletPath();
		url = utils.removeRootURL(url, utils.joinURL(data.reportingReqPath, REST_PATH_URL));
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
	 * @return the restServices
	 */
	public List<RestService> getRestServices() {
		return restServices;
	}

	/**
	 * @param restServices the restServices to set
	 */
	public void setRestServices(final List<RestService> restServices) {
		this.restServices = restServices;
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


}
