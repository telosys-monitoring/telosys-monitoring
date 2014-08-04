package org.telosys.webtools.monitoring.dispatch.reporting.text;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.monitor.MonitorData;


public class TextReportingTest {

	@Test
	public void testReporting() throws IOException {
		// Given
		final TextReporting textReporting = new TextReporting();
		final MonitorData monitorData = new MonitorData();

		final List<Request> lines = new ArrayList<Request>();
		final Request r1 = new Request();
		lines.add(r1);
		r1.startTime = 11000;
		r1.elapsedTime = 12;
		r1.pathInfo = "pathInfo1";
		r1.queryString = "queryString1";
		r1.requestURL = "requestURL1";
		r1.servletPath = "servletPath1";
		r1.countLongTimeRequests = 1;
		r1.countAllRequest = 5;
		final Request r2 = new Request();
		lines.add(r2);
		r2.startTime = 21000;
		r2.elapsedTime = 22;
		r2.pathInfo = "pathInfo2";
		r2.queryString = "queryString2";
		r2.requestURL = "requestURL2";
		r2.servletPath = "servletPath2";
		r2.countLongTimeRequests = 2;
		r2.countAllRequest = 10;

		monitorData.logLines = mock(CircularStack.class);
		when(monitorData.logLines.getAllAscending()).thenReturn(lines);

		monitorData.topRequests = mock(TopRequests.class);
		when(monitorData.topRequests.getAllDescending()).thenReturn(lines);

		monitorData.longestRequests = mock(LongestRequests.class);
		when(monitorData.longestRequests.getAllDescending()).thenReturn(lines);

		final HttpServletResponse response = mock(HttpServletResponse.class);
		final PrintWriter out = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(out);

		// When
		textReporting.reporting(response, monitorData);

		// Then
		verify(response).setContentType("text/plain");
		verify(response).setHeader("Pragma", "no-cache");
		verify(response).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		verify(response).setDateHeader ("Expires", 0);

		verify(monitorData.logLines).getAllAscending();
		verify(monitorData.topRequests).getAllDescending();
		verify(monitorData.longestRequests).getAllDescending();

		verify(out).println("IP address : " + monitorData.ipAddress);
		verify(out).println("Hostname : " + monitorData.hostname);
		verify(out).println("Duration threshold : " + monitorData.durationThreshold);
		verify(out).println("Log in memory size : " + monitorData.logSize + " lines");
		verify(out).println("Top requests by time : " + monitorData.topTenSize + " lines");
		verify(out).println("Top requests by URL : " + monitorData.longestSize + " lines");
		verify(out).println("Initialization date/time : " + monitorData.initializationDate);
		verify(out).println("Total requests count     : " + monitorData.countAllRequest);
		verify(out).println("Long time requests count : " + monitorData.countLongTimeRequests);
		verify(out).println("Last longest requests : " );
		verify(out).println("1970/01/01 01:00:11 - [ 1 / 5 ] - 12 ms - requestURL1?queryString1");
		verify(out).println("1970/01/01 01:00:21 - [ 2 / 10 ] - 22 ms - requestURL2?queryString2");
		verify(out).println("Top requests by time : " );
		verify(out).println("Top requests by URL : " );
		verify(out, times(2)).println("1970/01/01 01:00:11 - 12 ms - requestURL1?queryString1");
		verify(out, times(2)).println("1970/01/01 01:00:21 - 22 ms - requestURL2?queryString2");
	}

}
