package org.telosys.webtools.monitoring.dispatch.parameter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Get URL parameters.
 */
public class GetParameters {

	/**
	 * Get request parameters.
	 * @param request Request
	 * @return Map of parameters
	 */
	public Map<String, String> getParameters(final HttpServletRequest httpServletRequest) {
		final Map<String, String> params = new HashMap<String, String>();

		addRequestParameters(params, httpServletRequest);
		addRequestAttributes(params, httpServletRequest);

		return params;
	}

	/**
	 * Parse URL query string to get parameters and add them to the params map.
	 * @param params Parameters
	 * @param request Request
	 * @return Map of parameters
	 */
	protected Map<String, String> addRequestParameters(final Map<String, String> params, final HttpServletRequest httpServletRequest) {

		final String query = httpServletRequest.getQueryString();
		if(query == null) {
			return params;
		}

		final String[] querySplitteds = query.split("&");
		for(final String querySplitted : querySplitteds) {
			if((querySplitted == null) || "".equals(querySplitted.trim())) {
				continue;
			}
			final int posEquals = querySplitted.indexOf('=');
			if((posEquals == -1) || ((posEquals + 1) >= querySplitted.length())) {
				continue;
			}
			final String key = querySplitted.substring(0, posEquals);
			final String value = querySplitted.substring(posEquals + 1);
			params.put(key, value);
		}

		return params;
	}

	/**
	 * Parse URL query string to get parameters.
	 * @param params Parameters
	 * @param request Request
	 * @return Map of parameters
	 */
	@SuppressWarnings("rawtypes")
	protected void addRequestAttributes(final Map<String, String> params, final HttpServletRequest httpServletRequest) {

		final Enumeration attributeNames = httpServletRequest.getAttributeNames();
		if(attributeNames == null) {
			System.out.println("No request attributes");
			return;
		}

		System.out.println("Attributes : "+httpServletRequest.getAttributeNames());
		while(attributeNames.hasMoreElements()) {
			final String attributeName = (String) attributeNames.nextElement();
			final Object attributeValue = httpServletRequest.getAttribute(attributeName);
			System.out.println(" - "+attributeName+" : "+attributeValue);
			if(attributeValue != null) {
				params.put(attributeName, attributeValue.toString());
			}
		}
	}

}
