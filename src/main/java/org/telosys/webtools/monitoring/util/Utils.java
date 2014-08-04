package org.telosys.webtools.monitoring.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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
	 * Parse URL query string to get parameters.
	 * @param request Request
	 * @return Map of parameters
	 */
	public Map<String, String> getParameters(final HttpServletRequest httpServletRequest) {
		final Map<String, String> params = new HashMap<String, String>();

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

}
