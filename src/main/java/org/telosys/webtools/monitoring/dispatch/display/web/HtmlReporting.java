package org.telosys.webtools.monitoring.dispatch.display.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.telosys.webtools.monitoring.MonitorVersion;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.display.Controller;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.util.Utils;

/**
 * Report as HTML page with CSS style.
 */
public class HtmlReporting implements Controller {

	/**
	 * Action bar.
	 */
	protected ActionBarForReporting actionBarForReporting = new ActionBarForReporting();

	/**
	 * Utils.
	 */
	protected Utils utils = new Utils();

	/**
	 * Indicates if URL paths match to this manager.
	 * @param paths URL paths
	 * @param params URL parameters
	 * @return boolean
	 */
	public boolean match(final String[] paths, final Map<String, String> params) {
		return (paths == null) || (paths.length == 0);
	}

	/**
	 * Process.
	 * @param httpServletRequest Request
	 * @param httpServletResponse Response
	 * @param paths URL paths
	 * @param params URL parameters
	 * @param data Monitor data
	 * @param initValues Init values
	 */
	public void process(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse,
			final String[] paths,
			final Map<String, String> params,
			final MonitorData data,
			final MonitorInitValues initValues) {

		httpServletResponse.setContentType("text/html");

		//--- Prevent caching
		httpServletResponse.setHeader("Pragma", "no-cache"); // Set standard HTTP/1.0 no-cache header.
		httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // Set standard HTTP/1.1 no-cache header.
		httpServletResponse.setDateHeader ("Expires", 0); // Prevents caching on proxies

		final String date = utils.format( new Date() ) ;
		PrintWriter out;
		try {
			out = httpServletResponse.getWriter();

			out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			out.println("<html>");
			out.println("<head>");
			out.println("<style>");
			out.println("body{font-family:monospace;font-size:13px;margin:0;padding:0}");
			out.println("div{margin:5px}");
			out.println(".title{width:100%;min-width:800px;margin:0;background-color:#bfbfbf;border-bottom:1px solid #8f8f8f;} .title h1{width:800px;margin:0;padding:20px 0 0 20px;} .title .version{width:800px;margin:0;padding:2px 0 2px 20px;font-size:13px;color:#4f4f4f;font-weight:bold;}");
			out.println(".actionbar{width:100%;min-width:800px;margin:0;background-color:#f0f0f0;border-bottom:1px solid #bfbfbf;border-top:1px solid #ffffff;}");
			out.println(".actionbar .content{min-width:800px;margin:0;padding:5px;}");
			out.println(".main{min-width:800px;margin:0;}");
			out.println(".started{color:#009900;font-weight:bold;} .stopped{color:#990000;font-weight:bold;}");
			out.println("h2{font-size:15px;}");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");

			out.println("<div class='title'><h1>Telosys Monitoring</h1><div class='version'>"+MonitorVersion.VERSION+"</div></div>");

			actionBarForReporting.addActionBar(out, data);

			out.println("<div class='main'>");

			out.println("<div>");
			out.println("Requests monitoring status (" + date + ") : ");
			if(data.activated) {
				out.println("<span class='started'>[Started]</span>");
			} else {
				out.println("<span class='stopped'>[Stopped]</span>");
			}
			out.println("</div>");

			out.println("<div style='position:relative'>");

			out.println("<div style='float:left'>");
			out.println("<h2>Host</h2>" );
			out.println("<ul>");
			out.println("<li>IP address : " + data.ipAddress + "</li>");
			out.println("<li>Hostname : " + data.hostname + "</li>");
			out.println("<li>Java : "+System.getProperty("java.version")+" - "+System.getProperty("java.vendor")+"</li>");
			out.println("<li>OS : "+System.getProperty("os.arch")+" - "+System.getProperty("os.name")+" - "+System.getProperty("os.version")+"</li>");
			out.println("</ul>");
			out.println("</div>");

			out.println("<div style='float:left'>");
			out.println("<h2>Monitoring</h2>" );
			out.println("<ul>");
			out.println("<li>Init date/time : " + data.initializationDate + "</li>");
			out.println("<li>Total requests count     : " + data.countAllRequests + "</li>");
			out.println("<li>Long time requests count : " + data.countLongTimeRequests + "</li>");
			out.println("</ul></div>");

			out.println("<div style='float:left'>");
			out.println("<h2>Configuration</h2>" );
			out.println("<ul>");
			out.println("<li>Duration threshold : " + data.durationThreshold + " ms</li>");
			out.println("<li>Latest requests log size : " + data.latestSize + " lines</li>" );
			out.println("<li>Longest requests log size : " + data.longestSize + " lines</li>" );
			out.println("<li>Longest requests by URL log size : " + data.longestByUrlTempSize + " lines</li>" );
			out.println("<li>Catch URL parameters : " + (data.urlParamsActivated?"true":"false") + " </li>" );
			out.println("<li>URL parameters names : " + utils.mergeToString(data.urlParamsFilter, ',') + " </li>" );
			out.println("<li>Show empty URL param : " + (data.urlParamsEmpty?"true":"false") + " </li>" );
			out.println("</ul></div>");

			out.println("</div>");

			out.println("<div style='clear:both'></div>");

			out.println("<div style=\"width:100%\">");
			List<Request> requests = data.latestLines.getAllAscending();
			out.println("<h2>Latest requests</h2>" );
			out.println("<p>");
			for ( final Request request : requests ) {
				if(request != null) {
					out.print(request.toString());
					out.println(request.toStringUrlParameters(data.urlParamsEmpty));
					out.println("<br/>");
				}
			}
			out.println("</p>");
			out.println("</div>");

			out.println("<div style=\"width:100%\">");
			requests = data.longestRequests.getAllDescending();
			out.println(" ");
			out.println("<h2>Longest requests</h2>" );
			out.println("<p>");
			for ( final Request request : requests ) {
				if(request != null) {
					out.print(request.toStringWithoutCounting());
					out.println(request.toStringUrlParameters(data.urlParamsEmpty));
					out.println("<br/>");
				}
			}
			out.println("</p>");
			out.println("</div>");

			out.println("<div style=\"width:100%\">");
			requests = data.longestByUrlTempRequests.getAllDescending();
			out.println("<h2>Longest requests by URL</h2>" );
			out.println("<p>");
			for ( final Request request : requests ) {
				if(request != null) {
					out.print(request.toStringWithoutCounting());
					out.println(request.toStringUrlParameters(data.urlParamsEmpty));
					out.println("<br/>");
				}
			}
			out.println("</p>");
			out.println("</div>");

			out.println("</div>");

			actionBarForReporting.addActionBarJS(out, data);

			out.println("</body>");
			out.println("</html>");
			out.close();
		} catch (final IOException e) {
			throw new RuntimeException("RequestMonitor error : cannot get writer");
		}
	}
}
