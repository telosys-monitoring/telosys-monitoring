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
package org.telosys.webtools.monitoring.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Request.
 */
public class Request implements Serializable {

	private static final long serialVersionUID = -2713511325247998819L;

	/** Start date */
	public long startTime;
	/** Execution time */
	public long elapsedTime;
	/** URL */
	public String requestURL;
	/** URL params */
	public String queryString;
	/** Servlet path */
	public String servletPath;
	/** URL Path */
	public String pathInfo;
	/** Counting all requests */
	public long countAllRequest;
	/** Counting all longest requests */
	public long countLongTimeRequests;
	/** URL parameters */
	public Map<String, String> urlParams = new HashMap<String, String>();

	/**
	 * Constructor
	 */
	public Request() {
	}

	/**
	 * Convert to String value.
	 * @return string value
	 */
	@Override
	public String toString() {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final String sStartTime = dateFormat.format( new Date(startTime) ) ;
		final StringBuilder sb = new StringBuilder();
		sb.append(sStartTime );
		sb.append(" - [ ");
		sb.append(countLongTimeRequests);
		sb.append(" / ");
		sb.append(countAllRequest);
		sb.append(" ] - ");
		sb.append(elapsedTime);
		sb.append(" ms - ");
		sb.append(requestURL );
		if ( queryString != null ) {
			sb.append("?"+queryString );
		}
		return sb.toString();
	}

	/**
	 * Convert to String value without counting information.
	 * @return string value
	 */
	public String toStringWithoutCounting() {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final String sStartTime = dateFormat.format( new Date(startTime) ) ;
		final StringBuilder sb = new StringBuilder();
		sb.append(sStartTime );
		sb.append(" - ");
		sb.append(elapsedTime);
		sb.append(" ms - ");
		sb.append(requestURL );
		if ( queryString != null ) {
			sb.append("?"+queryString );
		}
		return sb.toString();
	}

	/**
	 * Display URL parameters.
	 * @param urlParamsEmpty Show empty URL parameters
	 * @return String output
	 */
	public String toStringUrlParameters(final boolean urlParamsEmpty) {
		final StringBuilder sb = new StringBuilder();
		if ( this.urlParams.size() > 0 ) {
			sb.append(" - URL params : [ ");
			boolean isFirst = true;
			for(final String urlParamKey : urlParams.keySet()) {
				// filter empty values
				final String urlParamValue = urlParams.get(urlParamKey);
				if(!urlParamsEmpty) {
					if((urlParamValue == null) || "".equals(urlParamValue.trim())) {
						continue;
					}
				}
				// display parameters
				if(isFirst) {
					isFirst = false;
				} else {
					sb.append(", ");
				}
				if(urlParamValue == null) {
					sb.append(urlParamKey + " = ");
				} else {
					sb.append(urlParamKey + " = \"" + urlParamValue+"\"");
				}
			}
			sb.append(" ]");
		}
		return sb.toString();
	}

	/**
	 * Return complete URL of the request including URL parameters.
	 * @return Complete URL as String value
	 */
	public String getURL() {
		if ( queryString != null ) {
			return requestURL + "?" + queryString;
		} else {
			return requestURL;
		}
	}

}
