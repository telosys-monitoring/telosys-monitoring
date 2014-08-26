/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.webtools.monitoring;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.Dispatch;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.monitor.MonitorInitValuesManager;
import org.telosys.webtools.monitoring.util.Log;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * Servlet Filter for Http Requests Monitor
 */
public class RequestsMonitor implements Filter {

	/** Numéro de version */
	public final static String VERSION = "v1.8";

	/** Configuration in the web.xml */
	protected MonitorInitValuesManager monitorWebXmlManager = new MonitorInitValuesManager();

	/** Dispatch request */
	protected Dispatch dispatch = new Dispatch();

	/** Utils */
	protected Utils utils = new Utils();

	/** Log */
	protected Log log = new Log();

	/** Request monitor data */
	protected MonitorData data = new MonitorData();

	/** Init values from web.xml configuration. */
	protected MonitorInitValues initValues;

	/** Count limit */
	public final static long COUNT_LIMIT                =  1000000;

	/**
	 * Default constructor.
	 */
	public RequestsMonitor() {
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(final FilterConfig filterConfig) throws ServletException {
		// Init values from web.xml
		this.initValues = monitorWebXmlManager.initValues(filterConfig);

		// Validate init values
		if(utils.isBlank(this.initValues.reportingReqPath)) {
			throw new ServletException("URL path of the report page is not defined. Please verify in the web.xml the value of the init-param 'reporting' which must not be empty.");
		}

		// Reset monitor data
		monitorWebXmlManager.reset(this.initValues, this.data);

		System.out.println("Telosys monitoring initialized");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(final ServletRequest servletRequest, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		if(this.data.traceFlag) {
			final HttpServletRequest request = (HttpServletRequest) servletRequest;
			System.out.println("=====");
			System.out.println("URL : "+request.getRequestURI());
			System.out.println(">>> Before");
			System.out.println("Parameters :");
			System.out.println(request.getAttributeNames());
			final Enumeration<?> parameterNamesEnumeration = request.getParameterNames();
			while(parameterNamesEnumeration.hasMoreElements()) {
				final String paramName = (String) parameterNamesEnumeration.nextElement();
				System.out.println(paramName + " : "+request.getParameter(paramName));
			}
			System.out.println("<<< Before");
			System.out.println("=====");
		}

		// Check if this request is to display the report page
		boolean isRequestForReportPage = false;
		try {
			isRequestForReportPage = isRequestForReportPage(servletRequest);
		} catch(final Throwable throwable) {
			log.manageError(throwable);
		}

		if( isRequestForReportPage ) {
			// Report page
			dispatch.dispatch( (HttpServletRequest) servletRequest, (HttpServletResponse) response, data, initValues );
		}
		else {

			// Check if monitoring is not activated
			if(!this.data.activated) {

				// "doFilter" method without request monitoring
				doFilterWithoutMonitoring(servletRequest, response, chain);

			} else {

				// "doFilter" method with request monitoring
				doFilterWithMonitoring(servletRequest, response, chain);

			}
		}

		if(this.data.traceFlag) {
			final HttpServletRequest request = (HttpServletRequest) servletRequest;
			System.out.println("=====");
			System.out.println("URL : "+request.getRequestURI());
			System.out.println(">>> After");
			System.out.println("Parameters :");
			System.out.println(request.getAttributeNames());
			final Enumeration parameterNamesEnumeration = request.getParameterNames();
			while(parameterNamesEnumeration.hasMoreElements()) {
				final String paramName = (String) parameterNamesEnumeration.nextElement();
				System.out.println(" - " + paramName + " : "+request.getParameter(paramName));
			}
			System.out.println("---");
			System.out.println("Attributes :");
			final Enumeration attributeNamesEnumeration = request.getAttributeNames();
			while(attributeNamesEnumeration.hasMoreElements()) {
				final String attrName = (String) attributeNamesEnumeration.nextElement();
				System.out.println(" - " + attrName + " : "+request.getAttribute(attrName));
			}
			System.out.println("---");
			System.out.println("=> Request content :");
			String strLine;
			while((strLine = request.getReader().readLine())!= null)
			{
				System.out.println(strLine);
			}
			System.out.println("<<< After");
			System.out.println("=====");
		}
	}

	/**
	 * "doFilter" method without request monitoring.
	 * @param request HTTP request
	 * @param response HTTP response
	 * @param chain Filter chain
	 * @throws IOException Error
	 * @throws ServletException Error
	 */
	protected void doFilterWithoutMonitoring(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		//--- Chain (nothing to stop here)
		chain.doFilter(request, response);

	}

	/**
	 * "doFilter" method with request monitoring.
	 * @param request HTTP request
	 * @param response HTTP response
	 * @param chain Filter chain
	 * @throws IOException Error
	 * @throws ServletException Error
	 */
	protected void doFilterWithMonitoring(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		long startTime = 0;

		try {
			incrementCountAllRequest();
			startTime = utils.getTime();
		} catch(final Throwable throwable) {
			log.manageError(throwable);
		}

		try {

			//--- Chain (nothing to stop here)
			chain.doFilter(request, response);

		} finally {
			try {
				final long elapsedTime = utils.getTime() - startTime;
				if ( elapsedTime > data.durationThreshold ) {
					logRequest(request, startTime, elapsedTime);
				}
			} catch(final Throwable throwable) {
				log.manageError(throwable);
			}
		}
	}

	/**
	 * Indicates if the request is to access to the report page.
	 * @param httpRequest HTTP request.
	 * @return boolean.
	 */
	protected boolean isRequestForReportPage(final ServletRequest servletRequest) {
		if(!(servletRequest instanceof HttpServletRequest)) {
			return false;
		}
		if(utils.isBlank(data.reportingReqPath)) {
			return false;
		}
		final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		final String pathInfo = utils.getURI(httpServletRequest);
		return ((pathInfo != null) && pathInfo.startsWith(data.reportingReqPath));
	}

	/**
	 * Increment count all requests.
	 */
	protected synchronized long incrementCountAllRequest() {
		data.countAllRequest = data.countAllRequest + 1;
		if(data.countAllRequest > COUNT_LIMIT) {
			data.countAllRequest = 1;
		}
		return data.countAllRequest;
	}

	/**
	 * Increment count all long time requests.
	 */
	protected synchronized void incrementCountLongTimeRequests(final Request request) {
		data.countLongTimeRequests = data.countLongTimeRequests+1;
		if(data.countLongTimeRequests > COUNT_LIMIT) {
			data.countLongTimeRequests = 1;
		}
		request.countLongTimeRequests = data.countLongTimeRequests;
		data.countAllRequestForRequest = data.countAllRequestForRequest+1;
		if(data.countAllRequestForRequest > COUNT_LIMIT) {
			data.countAllRequestForRequest = 1;
		}
		request.countAllRequest = data.countAllRequestForRequest;
	}

	/**
	 * Create Request object and stores this request.
	 * @param httpRequest HTTP request
	 * @param startTime Start date
	 * @param elapsedTime Execution time
	 * @param countAllRequest Count all resquests
	 * @param countLongTimeRequests Count Longest requests
	 */
	protected final void logRequest(final ServletRequest servletRequest, final long startTime, final long elapsedTime ) {
		final Request request = createRequest(servletRequest, startTime, elapsedTime);

		data.logLines.add(request);
		data.topRequests.add(request);
		data.longestRequests.add(request);

		log.trace(request);
	}

	/**
	 * Create request
	 * @param httpRequest HTTP request
	 * @param startTime Start date
	 * @param elapsedTime Request execution time
	 * @param countAllRequest Count all requests
	 * @param countLongTimeRequests Count longest requests
	 * @return request
	 */
	protected Request createRequest(final ServletRequest servletRequest, final long startTime, final long elapsedTime) {
		final Request request = new Request();
		request.elapsedTime = elapsedTime;
		request.startTime = startTime;
		if(servletRequest instanceof HttpServletRequest) {
			final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			request.pathInfo = httpServletRequest.getPathInfo();
			request.queryString = httpServletRequest.getQueryString();
			request.requestURL = httpServletRequest.getRequestURL().toString();
			request.servletPath = httpServletRequest.getServletPath();
		} else {
			request.pathInfo = "";
			request.queryString = "";
			request.requestURL = "";
			request.servletPath = "";
		}
		this.incrementCountLongTimeRequests(request);
		return request;
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @return the monitorWebXmlManager
	 */
	public MonitorInitValuesManager getMonitorWebXmlManager() {
		return monitorWebXmlManager;
	}

	/**
	 * @param monitorWebXmlManager the monitorWebXmlManager to set
	 */
	public void setMonitorWebXmlManager(final MonitorInitValuesManager monitorWebXmlManager) {
		this.monitorWebXmlManager = monitorWebXmlManager;
	}

	/**
	 * @return the dispatch
	 */
	public Dispatch getDispatch() {
		return dispatch;
	}

	/**
	 * @param dispatch the dispatch to set
	 */
	public void setDispatch(final Dispatch dispatch) {
		this.dispatch = dispatch;
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
