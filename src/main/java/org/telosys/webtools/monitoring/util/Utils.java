package org.telosys.webtools.monitoring.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private final String EMPTY = "";

	/**
	 * Get request parameters.
	 * @param request Request
	 * @return Map of parameters
	 */
	public Map<String, String> getParameters(final HttpServletRequest httpServletRequest) {
		final Map<String, String> params = new HashMap<String, String>();

		final Enumeration<?> parameterNames = httpServletRequest.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			final String parameterName = (String) parameterNames.nextElement();
			params.put(parameterName, httpServletRequest.getParameter(parameterName));
		}

		return params;
	}

	/**
	 * Return URI without web application name.
	 * @param httpServletRequest request
	 * @return URI
	 */
	public String getURI(final HttpServletRequest httpServletRequest) {
		String uri = httpServletRequest.getRequestURI();
		if(uri == null) {
			return EMPTY;
		}
		if(httpServletRequest.getContextPath() != null) {
			uri = uri.substring(httpServletRequest.getContextPath().length());
		}
		if(uri == null) {
			return EMPTY;
		}
		return uri;
	}

	/**
	 * Return IP address and hostname.
	 * @return IP address and hostname
	 */
	public InetAddress getLocalHost() {
		try {
			return InetAddress.getLocalHost();
		} catch (final UnknownHostException e) {
			return null;
		}
	}

	/**
	 * @return current time in milliseconds
	 *
	 */
	public long getTime() {
		// Uses System.nanoTime() if necessary (precision ++)
		return System.currentTimeMillis();
	}

	/**
	 * Convert String value to Integer.
	 * @param s String value
	 * @param defaultValue Default Integer value if the conversion fails
	 * @return Integer value
	 */
	public int parseInt(final String s, final int defaultValue) {
		int v = defaultValue ;
		if ( s != null ) {
			try {
				v = Integer.parseInt( s ) ;
			} catch (final NumberFormatException e) {
				v = defaultValue ;
			}
		}
		return v ;
	}

	/**
	 * Convert Date to String value.
	 * @param date Date
	 * @return String value
	 */
	public final String format ( final Date date ) {
		return dateFormat.format( date ) ;
	}

	/**
	 * Returns true if the text is blank or null.
	 * @param txt text
	 * @return is blank or null
	 */
	public boolean isBlank(final String txt) {
		return (txt == null) || "".equals(txt.trim());
	}

	/**
	 * Trim string value, and return empty if null
	 * @param txt Text
	 * @return result
	 */
	public String trimToNull(String txt) {
		if(txt == null) {
			return null;
		}
		txt = txt.trim();
		if("".equals(txt)) {
			return null;
		}
		return txt;
	}

	/**
	 * Trim string value, and return empty if null
	 * @param txt Text
	 * @return result
	 */
	public String trimToEmpty(final String txt) {
		if(txt == null) {
			return "";
		}
		return txt.trim();
	}

	/**
	 * Join URL and path.
	 * @param url URL
	 * @param path path
	 * @return URL with path
	 */
	public String joinURL( String url, String path) {
		final StringBuffer result = new StringBuffer();

		url = trimToEmpty(url);
		if((url.length() > 0) && (url.charAt(url.length()-1) == '/')) {
			result.append(url.substring(0, url.length()-1));
		} else {
			result.append(url);
		}

		path = trimToEmpty(path);
		if(path.length() > 0) {
			if(path.charAt(0) != '/') {
				result.append('/');
			}
			result.append(path);
		}

		return result.toString();
	}

	/**
	 * Remove root of URL.
	 * @param url URL
	 * @param path path
	 * @return URL with path
	 */
	public String removeRootURL(String url, String root) {
		if(url == null) {
			return null;
		}
		url = trimToEmpty(url);
		if(url.length() == 0) {
			return url;
		}

		root = trimToEmpty(root);

		String result;
		if(url.startsWith(root)) {
			result = url.substring(root.length());
		}
		else if(url.startsWith('/' + root)) {
			result = url.substring(root.length()+1);
		}
		else if(('/'+url).startsWith(root)) {
			result = url.substring(root.length()-1);
		}
		else {
			result = url;
		}
		if(result.length() > 0) {
			if(result.charAt(0) == '/') {
				result = result.substring(1);
			}
		}
		return result;
	}

	/**
	 * Split URL to URL paths array.
	 * @param url URL
	 * @return URL paths array
	 */
	public String[] splitURLPaths(final String url) {
		return split(url, '/');
	}

	/**
	 * Split URL to URL paths array.
	 * @param url URL
	 * @return URL paths array
	 */
	public String[] split(String url, final char separator) {
		url = trimToEmpty(url);
		if(url.length() == 0) {
			return new String[0];
		}
		else if(url.length() == 1) {
			if(url.charAt(0) == separator) {
				return new String[0];
			}
			return new String[] {url};
		} else {
			if(url.charAt(0) == separator) {
				url = url.substring(1);
			}
			return url.split(String.valueOf(separator));
		}
	}

}
