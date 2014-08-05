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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.monitor.MonitorInitValuesManager;

public class RequestsMonitorTest {

	@Test
	public void testInit() {
		// Given
		final RequestsMonitor requestsMonitor = new RequestsMonitor();
		requestsMonitor.utils = spy(requestsMonitor.utils);

		requestsMonitor.monitorWebXmlManager = mock(MonitorInitValuesManager.class);

		final FilterConfig filterConfig = mock(FilterConfig.class);
		final MonitorInitValues initValues = new MonitorInitValues();
		doReturn(initValues).when(requestsMonitor.monitorWebXmlManager).initValues(filterConfig);

		requestsMonitor.data = mock(MonitorData.class);

		// When
		try {
			requestsMonitor.init(filterConfig);
		} catch(final ServletException e) {
			fail("initialisation failed : "+e.getMessage());
		}

		// Then
		verify(requestsMonitor.monitorWebXmlManager).reset(initValues, requestsMonitor.data);
	}

	@Test
	public void testInitFailed() {
		// Given
		final RequestsMonitor requestsMonitor = new RequestsMonitor();
		requestsMonitor.utils = spy(requestsMonitor.utils);
		doReturn(true).when(requestsMonitor.utils).isBlank(null);

		requestsMonitor.monitorWebXmlManager = mock(MonitorInitValuesManager.class);

		final FilterConfig filterConfig = mock(FilterConfig.class);
		final MonitorInitValues initValues = new MonitorInitValues();
		doReturn(initValues).when(requestsMonitor.monitorWebXmlManager).initValues(filterConfig);

		initValues.reportingReqPath = null;

		// When
		ServletException servletException = null;
		try {
			requestsMonitor.init(filterConfig);
			fail("a ServletException must be throwned");
		} catch(final ServletException e) {
			servletException = e;
		}

		// Then
		assertEquals("URL path of the report page is not defined. Please verify in the web.xml the value of the init-param 'reporting' which must not be empty.", servletException.getMessage());
	}

	@Test
	public void testDoFilterActivated() throws IOException, ServletException {
		// Given
		final RequestsMonitor requestsMonitor = new RequestsMonitor();
		requestsMonitor.utils = spy(requestsMonitor.utils);

		final MonitorData data = requestsMonitor.data;
		data.activated = true;
		data.reportingReqPath = "/monitoring";
		data.durationThreshold = -99999999;
		data.countAllRequest = RequestsMonitor.COUNT_LIMIT-1;
		data.countAllRequestForRequest = RequestsMonitor.COUNT_LIMIT-1;
		data.countLongTimeRequests = RequestsMonitor.COUNT_LIMIT-1;
		data.logLines = new CircularStack(200);
		data.topRequests = new TopRequests(200);
		data.longestRequests = new LongestRequests(200);

		when(requestsMonitor.utils.getTime()).thenAnswer(new Answer<Long>() {
			private long time = 0;
			public Long answer(final InvocationOnMock invocation) throws Throwable {
				time += 500;
				return time;
			}
		});

		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);

		final HttpServletRequest httpRequest1 = mock(HttpServletRequest.class);
		when(httpRequest1.getServletPath()).thenReturn("/test1");
		when(httpRequest1.getRequestURL()).thenReturn(new StringBuffer("http://request1.url"));
		when(httpRequest1.getQueryString()).thenReturn("query1");

		final HttpServletRequest httpRequest2 = mock(HttpServletRequest.class);
		when(httpRequest2.getServletPath()).thenReturn("/test2");
		when(httpRequest2.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest2.getQueryString()).thenReturn("query2");

		// When
		requestsMonitor.doFilter(httpRequest1, response, chain);
		requestsMonitor.doFilter(httpRequest2, response, chain);

		// Then
		verify(chain).doFilter(httpRequest1, response);
		verify(chain).doFilter(httpRequest2, response);

		List<Request> requests = data.logLines.getAllAscending();
		assertEquals("1970/01/01 01:00:00 - [ "+RequestsMonitor.COUNT_LIMIT+" / "+RequestsMonitor.COUNT_LIMIT+" ] - 500 ms - http://request1.url?query1", requests.get(0).toString());
		assertEquals("1970/01/01 01:00:01 - [ 1 / 1 ] - 500 ms - http://request2.url?query2", requests.get(1).toString());

		requests = data.topRequests.getAllAscending();
		assertEquals("1970/01/01 01:00:00 - [ "+RequestsMonitor.COUNT_LIMIT+" / "+RequestsMonitor.COUNT_LIMIT+" ] - 500 ms - http://request1.url?query1", requests.get(0).toString());
		assertEquals("1970/01/01 01:00:01 - [ 1 / 1 ] - 500 ms - http://request2.url?query2", requests.get(1).toString());

		requests = data.longestRequests.getAllDescending();
		assertEquals("1970/01/01 01:00:01 - [ 1 / 1 ] - 500 ms - http://request2.url?query2", requests.get(0).toString());
		assertEquals("1970/01/01 01:00:00 - [ "+RequestsMonitor.COUNT_LIMIT+" / "+RequestsMonitor.COUNT_LIMIT+" ] - 500 ms - http://request1.url?query1", requests.get(1).toString());
	}

	@Test
	public void testDoFilterDesactivated() throws IOException, ServletException {
		// Given
		final RequestsMonitor requestsMonitor = spy(new RequestsMonitor());
		requestsMonitor.utils = spy(requestsMonitor.utils);

		final MonitorData data = requestsMonitor.data;
		data.activated = false;
		data.reportingReqPath = "/monitoring";
		data.durationThreshold = -99999999;
		data.logLines = new CircularStack(200);
		data.topRequests = new TopRequests(200);
		data.longestRequests = new LongestRequests(200);

		when(requestsMonitor.utils.getTime()).thenAnswer(new Answer<Long>() {
			private long time = 0;
			public Long answer(final InvocationOnMock invocation) throws Throwable {
				time += 500;
				return time;
			}
		});

		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);

		final HttpServletRequest httpRequest1 = mock(HttpServletRequest.class);
		when(httpRequest1.getServletPath()).thenReturn("/test1");
		when(httpRequest1.getRequestURL()).thenReturn(new StringBuffer("http://request1.url"));
		when(httpRequest1.getQueryString()).thenReturn("query1");

		final HttpServletRequest httpRequest2 = mock(HttpServletRequest.class);
		when(httpRequest2.getServletPath()).thenReturn("/test2");
		when(httpRequest2.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest2.getQueryString()).thenReturn("query2");

		// When
		requestsMonitor.doFilter(httpRequest1, response, chain);
		requestsMonitor.doFilter(httpRequest2, response, chain);

		// Then
		verify(chain).doFilter(httpRequest1, response);
		verify(chain).doFilter(httpRequest2, response);

		List<Request> requests = data.logLines.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.topRequests.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.longestRequests.getAllDescending();
		assertEquals(0, requests.size());
	}

	@Test
	public void testDoFilter_RestInfo() throws IOException, ServletException {
		// Given
		final RequestsMonitor requestsMonitor = spy(new RequestsMonitor());
		requestsMonitor.utils = spy(requestsMonitor.utils);

		final MonitorData data = requestsMonitor.data;
		data.activated = false;
		data.reportingReqPath = "/monitoring";
		data.durationThreshold = -99999999;
		data.logLines = new CircularStack(200);
		data.topRequests = new TopRequests(200);
		data.longestRequests = new LongestRequests(200);

		when(requestsMonitor.utils.getTime()).thenAnswer(new Answer<Long>() {
			private long time = 0;
			public Long answer(final InvocationOnMock invocation) throws Throwable {
				time += 500;
				return time;
			}
		});

		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);

		final HttpServletRequest httpRequest1 = mock(HttpServletRequest.class);
		when(httpRequest1.getServletPath()).thenReturn("/test1");
		when(httpRequest1.getRequestURL()).thenReturn(new StringBuffer("http://request1.url"));
		when(httpRequest1.getQueryString()).thenReturn("query1");

		final HttpServletRequest httpRequest2 = mock(HttpServletRequest.class);
		when(httpRequest2.getServletPath()).thenReturn("/test2");
		when(httpRequest2.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest2.getQueryString()).thenReturn("query2");

		final HttpServletRequest httpRequest3 = mock(HttpServletRequest.class);
		when(httpRequest3.getServletPath()).thenReturn(data.reportingReqPath+"/rest/info");
		when(httpRequest3.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest3.getQueryString()).thenReturn("");

		final PrintWriter printWriter = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(printWriter);
		final StringBuffer out = new StringBuffer();
		final Answer answer = new Answer() {
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] arguments = invocation.getArguments();
				out.append(arguments[0]);
				return null;
			}
		};
		doAnswer(answer).when(printWriter).print(anyString());

		// When
		requestsMonitor.doFilter(httpRequest1, response, chain);
		requestsMonitor.doFilter(httpRequest2, response, chain);
		requestsMonitor.doFilter(httpRequest3, response, chain);

		// Then
		verify(chain).doFilter(httpRequest1, response);
		verify(chain).doFilter(httpRequest2, response);

		List<Request> requests = data.logLines.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.topRequests.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.longestRequests.getAllDescending();
		assertEquals(0, requests.size());

		assertTrue(out.toString().indexOf("\"host\":") != -1);
	}

	@Test
	public void testDoFilter_RestLog() throws IOException, ServletException {
		// Given
		final RequestsMonitor requestsMonitor = spy(new RequestsMonitor());
		requestsMonitor.utils = spy(requestsMonitor.utils);

		final MonitorData data = requestsMonitor.data;
		data.activated = false;
		data.reportingReqPath = "/monitoring";
		data.durationThreshold = -99999999;
		data.logLines = new CircularStack(200);
		data.topRequests = new TopRequests(200);
		data.longestRequests = new LongestRequests(200);

		when(requestsMonitor.utils.getTime()).thenAnswer(new Answer<Long>() {
			private long time = 0;
			public Long answer(final InvocationOnMock invocation) throws Throwable {
				time += 500;
				return time;
			}
		});

		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);

		final HttpServletRequest httpRequest1 = mock(HttpServletRequest.class);
		when(httpRequest1.getServletPath()).thenReturn("/test1");
		when(httpRequest1.getRequestURL()).thenReturn(new StringBuffer("http://request1.url"));
		when(httpRequest1.getQueryString()).thenReturn("query1");

		final HttpServletRequest httpRequest2 = mock(HttpServletRequest.class);
		when(httpRequest2.getServletPath()).thenReturn("/test2");
		when(httpRequest2.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest2.getQueryString()).thenReturn("query2");

		final HttpServletRequest httpRequest3 = mock(HttpServletRequest.class);
		when(httpRequest3.getServletPath()).thenReturn(data.reportingReqPath+"/rest/log");
		when(httpRequest3.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest3.getQueryString()).thenReturn("");

		final PrintWriter printWriter = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(printWriter);
		final StringBuffer out = new StringBuffer();
		final Answer answer = new Answer() {
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] arguments = invocation.getArguments();
				out.append(arguments[0]);
				return null;
			}
		};
		doAnswer(answer).when(printWriter).print(anyString());

		// When
		requestsMonitor.doFilter(httpRequest1, response, chain);
		requestsMonitor.doFilter(httpRequest2, response, chain);
		requestsMonitor.doFilter(httpRequest3, response, chain);

		// Then
		verify(chain).doFilter(httpRequest1, response);
		verify(chain).doFilter(httpRequest2, response);

		List<Request> requests = data.logLines.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.topRequests.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.longestRequests.getAllDescending();
		assertEquals(0, requests.size());

		assertTrue(out.toString().indexOf("\"log\":") != -1);
	}

	@Test
	public void testDoFilter_RestTop() throws IOException, ServletException {
		// Given
		final RequestsMonitor requestsMonitor = spy(new RequestsMonitor());
		requestsMonitor.utils = spy(requestsMonitor.utils);

		final MonitorData data = requestsMonitor.data;
		data.activated = false;
		data.reportingReqPath = "/monitoring";
		data.durationThreshold = -99999999;
		data.logLines = new CircularStack(200);
		data.topRequests = new TopRequests(200);
		data.longestRequests = new LongestRequests(200);

		when(requestsMonitor.utils.getTime()).thenAnswer(new Answer<Long>() {
			private long time = 0;
			public Long answer(final InvocationOnMock invocation) throws Throwable {
				time += 500;
				return time;
			}
		});

		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);

		final HttpServletRequest httpRequest1 = mock(HttpServletRequest.class);
		when(httpRequest1.getServletPath()).thenReturn("/test1");
		when(httpRequest1.getRequestURL()).thenReturn(new StringBuffer("http://request1.url"));
		when(httpRequest1.getQueryString()).thenReturn("query1");

		final HttpServletRequest httpRequest2 = mock(HttpServletRequest.class);
		when(httpRequest2.getServletPath()).thenReturn("/test2");
		when(httpRequest2.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest2.getQueryString()).thenReturn("query2");

		final HttpServletRequest httpRequest3 = mock(HttpServletRequest.class);
		when(httpRequest3.getServletPath()).thenReturn(data.reportingReqPath+"/rest/top");
		when(httpRequest3.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest3.getQueryString()).thenReturn("");

		final PrintWriter printWriter = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(printWriter);
		final StringBuffer out = new StringBuffer();
		final Answer answer = new Answer() {
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] arguments = invocation.getArguments();
				out.append(arguments[0]);
				return null;
			}
		};
		doAnswer(answer).when(printWriter).print(anyString());

		// When
		requestsMonitor.doFilter(httpRequest1, response, chain);
		requestsMonitor.doFilter(httpRequest2, response, chain);
		requestsMonitor.doFilter(httpRequest3, response, chain);

		// Then
		verify(chain).doFilter(httpRequest1, response);
		verify(chain).doFilter(httpRequest2, response);

		List<Request> requests = data.logLines.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.topRequests.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.longestRequests.getAllDescending();
		assertEquals(0, requests.size());

		assertTrue(out.toString().indexOf("\"top\":") != -1);
	}

	@Test
	public void testDoFilter_RestLongest() throws IOException, ServletException {
		// Given
		final RequestsMonitor requestsMonitor = spy(new RequestsMonitor());
		requestsMonitor.utils = spy(requestsMonitor.utils);

		final MonitorData data = requestsMonitor.data;
		data.activated = false;
		data.reportingReqPath = "/monitoring";
		data.durationThreshold = -99999999;
		data.logLines = new CircularStack(200);
		data.topRequests = new TopRequests(200);
		data.longestRequests = new LongestRequests(200);

		when(requestsMonitor.utils.getTime()).thenAnswer(new Answer<Long>() {
			private long time = 0;
			public Long answer(final InvocationOnMock invocation) throws Throwable {
				time += 500;
				return time;
			}
		});

		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);

		final HttpServletRequest httpRequest1 = mock(HttpServletRequest.class);
		when(httpRequest1.getServletPath()).thenReturn("/test1");
		when(httpRequest1.getRequestURL()).thenReturn(new StringBuffer("http://request1.url"));
		when(httpRequest1.getQueryString()).thenReturn("query1");

		final HttpServletRequest httpRequest2 = mock(HttpServletRequest.class);
		when(httpRequest2.getServletPath()).thenReturn("/test2");
		when(httpRequest2.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest2.getQueryString()).thenReturn("query2");

		final HttpServletRequest httpRequest3 = mock(HttpServletRequest.class);
		when(httpRequest3.getServletPath()).thenReturn(data.reportingReqPath+"/rest/longest");
		when(httpRequest3.getRequestURL()).thenReturn(new StringBuffer("http://request2.url"));
		when(httpRequest3.getQueryString()).thenReturn("");

		final PrintWriter printWriter = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(printWriter);
		final StringBuffer out = new StringBuffer();
		final Answer answer = new Answer() {
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] arguments = invocation.getArguments();
				out.append(arguments[0]);
				return null;
			}
		};
		doAnswer(answer).when(printWriter).print(anyString());

		// When
		requestsMonitor.doFilter(httpRequest1, response, chain);
		requestsMonitor.doFilter(httpRequest2, response, chain);
		requestsMonitor.doFilter(httpRequest3, response, chain);

		// Then
		verify(chain).doFilter(httpRequest1, response);
		verify(chain).doFilter(httpRequest2, response);

		List<Request> requests = data.logLines.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.topRequests.getAllAscending();
		assertEquals(0, requests.size());

		requests = data.longestRequests.getAllDescending();
		assertEquals(0, requests.size());

		assertTrue(out.toString().indexOf("\"longest\":") != -1);
	}

}
