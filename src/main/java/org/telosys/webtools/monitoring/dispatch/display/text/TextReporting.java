package org.telosys.webtools.monitoring.dispatch.display.text;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * Report as simple text.
 */
public class TextReporting {

	/**
	 * Utils.
	 */
	protected Utils utils = new Utils();

	/**
	 * Reports the current status in plain text
	 * @param response HTTP response
	 */
	public void reporting (final HttpServletResponse response, final MonitorData data) {
		response.setContentType("text/plain");

		//--- Prevent caching
		response.setHeader("Pragma", "no-cache"); // Set standard HTTP/1.0 no-cache header.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // Set standard HTTP/1.1 no-cache header.
		response.setDateHeader ("Expires", 0); // Prevents caching on proxies

		final String date = utils.format( new Date() ) ;
		PrintWriter out;
		try {
			out = response.getWriter();

			out.println("Requests monitoring status (" + date + ") ");
			out.println("IP address : " + data.ipAddress);
			out.println("Hostname : " + data.hostname);
			out.println(" ");

			out.println("Duration threshold : " + data.durationThreshold );
			out.println("Log in memory size : " + data.logSize + " lines" );
			out.println("Top requests by time : " + data.topTenSize + " lines" );
			out.println("Top requests by URL : " + data.longestSize + " lines" );
			out.println(" ");

			out.println("Initialization date/time : " + data.initializationDate );
			out.println("Total requests count     : " + data.countAllRequest);
			out.println("Long time requests count : " + data.countLongTimeRequests );
			out.println(" ");

			List<Request> requests = data.logLines.getAllAscending();
			out.println("Last longest requests : " );
			for ( final Request request : requests ) {
				if(request != null) {
					out.println(request.toString());
				}
			}

			requests = data.topRequests.getAllDescending();
			out.println(" ");
			out.println("Top requests by time : " );
			for ( final Request request : requests ) {
				if(request != null) {
					out.println(request.toStringWithoutCounting());
				}
			}

			requests = data.longestRequests.getAllDescending();
			out.println(" ");
			out.println("Top requests by URL : " );
			for ( final Request request : requests ) {
				if(request != null) {
					out.println(request.toStringWithoutCounting());
				}
			}

			out.close();
		} catch (final IOException e) {
			throw new RuntimeException("RequestMonitor error : cannot get writer");
		}
	}
}
