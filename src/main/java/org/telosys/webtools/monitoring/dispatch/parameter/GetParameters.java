package org.telosys.webtools.monitoring.dispatch.parameter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Get URL parameters.
 */
public class GetParameters {

	/**
	 * Parse URL query string to get parameters.
	 * @param request Request
	 * @return Map of parameters
	 */
	public Map<String, String> getParameters(HttpServletRequest httpServletRequest) {
		Map<String, String> params = new HashMap<String, String>();
		
		String query = httpServletRequest.getQueryString();
		if(query == null) {
			return params;
		}
		
		String[] querySplitteds = query.split("&");
		for(String querySplitted : querySplitteds) {
			if(querySplitted == null || "".equals(querySplitted.trim())) {
				continue;
			}
			int posEquals = querySplitted.indexOf('=');
			if(posEquals == -1 || posEquals + 1 >= querySplitted.length()) {
				continue;
			}
			String key = querySplitted.substring(0, posEquals);
			String value = querySplitted.substring(posEquals + 1);
			params.put(key, value);
		}
		
		return params;
	}
	
}
