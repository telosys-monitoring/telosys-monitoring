package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.monitor.InitValues;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorWebXmlManager;
import org.telosys.webtools.monitoring.monitor.RequestAttributeNames;
import org.telosys.webtools.monitoring.monitor.Utils;


public class ActionTest {

	@Test
	public void testAction1() {
		// Given
		final Action action = new Action();
		final MonitorData data = new MonitorData();
		final InitValues initValues = new InitValues();

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.durationThreshold = 200;
		data.logSize = 300;
		data.topTenSize = 400;
		data.longestSize = 500;
		data.traceFlag = true;

		final Map<String,String> params = new HashMap<String,String>();
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD, "201");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "301");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "401");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "501");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG, "true");

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(201, data.durationThreshold);
		assertEquals(301, data.logSize);
		assertNotEquals(logLines, data.logLines);
		assertEquals(401, data.topTenSize);
		assertNotEquals(topRequests, data.topRequests);
		assertEquals(501, data.longestSize);
		assertNotEquals(longestRequests, data.longestRequests);
		assertTrue(data.traceFlag);

	}

	@Test
	public void testAction2() {
		// Given
		final Action action = new Action();
		final MonitorData data = new MonitorData();
		final InitValues initValues = new InitValues();

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.durationThreshold = 200;
		data.logSize = 300;
		data.topTenSize = 400;
		data.longestSize = 500;
		data.traceFlag = true;

		final Map<String,String> params = new HashMap<String,String>();
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD, "200");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "300");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "400");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "500");
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG, "false");

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(200, data.durationThreshold);
		assertEquals(300, data.logSize);
		assertEquals(logLines, data.logLines);
		assertEquals(400, data.topTenSize);
		assertEquals(topRequests, data.topRequests);
		assertEquals(500, data.longestSize);
		assertEquals(longestRequests, data.longestRequests);
		assertFalse(data.traceFlag);
	}

	@Test
	public void testActionStop() {
		// Given
		final Action action = new Action();
		final MonitorData data = new MonitorData();
		final InitValues initValues = new InitValues();

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.activated = true;

		final Map<String,String> params = new HashMap<String,String>();
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_ACTION, RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_STOP);

		// When
		action.action(params, data, initValues);

		// Then
		assertFalse(data.activated);
	}

	@Test
	public void testActionStart() {
		// Given
		final Action action = new Action();
		final MonitorData data = new MonitorData();
		final InitValues initValues = new InitValues();

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.activated = false;

		final Map<String,String> params = new HashMap<String,String>();
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_ACTION, RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_START);

		// When
		action.action(params, data, initValues);

		// Then
		assertTrue(data.activated);
	}

	@Test
	public void testActionClean() {
		// Given
		final Action action = new Action();
		final MonitorData data = new MonitorData();
		final InitValues initValues = new InitValues();

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.logSize = 300;
		data.topTenSize = 400;
		data.longestSize = 500;

		final Map<String,String> params = new HashMap<String,String>();
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_ACTION, RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_CLEAR);

		// When
		action.action(params, data, initValues);

		// Then
		assertNotEquals(logLines, data.logLines);
		assertNotEquals(topRequests, data.topRequests);
		assertNotEquals(longestRequests, data.longestRequests);
	}

	@Test
	public void testActionReset() throws ServletException {

		/**
		 * 1: filter initialization
		 */

		// Given
		final MonitorWebXmlManager monitorWebXmlManager = new MonitorWebXmlManager();
		final Utils utils = spy(new Utils());
		monitorWebXmlManager.setUtils(utils);

		final InetAddress adrLocale = mock(InetAddress.class);
		doReturn(adrLocale).when(utils).getLocalHost();
		when(adrLocale.getHostAddress()).thenReturn("10.11.12.13");
		when(adrLocale.getHostName()).thenReturn("hostname");

		final FilterConfig filterConfig = mock(FilterConfig.class);

		// When
		final InitValues initValues = monitorWebXmlManager.initValues(filterConfig);
		final MonitorData data = new MonitorData();
		monitorWebXmlManager.reset(initValues, data);

		// Then
		assertEquals(InitValues.DEFAULT_DURATION_THRESHOLD, data.durationThreshold);
		assertEquals(InitValues.DEFAULT_LOG_SIZE, data.logSize);
		assertEquals(InitValues.DEFAULT_TOP_TEN_SIZE, data.topTenSize);
		assertEquals(InitValues.DEFAULT_LONGEST_SIZE, data.longestSize);
		assertEquals("/monitor", data.reportingReqPath);
		assertFalse(data.traceFlag);
		assertEquals("10.11.12.13", data.ipAddress);
		assertEquals("hostname", data.hostname);

		/**
		 * 2: filter update
		 */

		data.durationThreshold = 1;
		data.logSize = 2;
		data.topTenSize = 3;
		data.longestSize = 4;
		data.reportingReqPath = "test";
		data.traceFlag = true;
		data.ipAddress = "1.2.3.4";
		data.hostname = "test2";

		/**
		 * 3: filter reset
		 */

		// Given
		final Action action = new Action();
		action.monitorWebXmlManager = monitorWebXmlManager;

		final Map<String,String> params = new HashMap<String,String>();
		params.put(RequestAttributeNames.ATTRIBUTE_NAME_ACTION, RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_RESET);

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(InitValues.DEFAULT_DURATION_THRESHOLD, data.durationThreshold);
		assertEquals(InitValues.DEFAULT_LOG_SIZE, data.logSize);
		assertEquals(InitValues.DEFAULT_TOP_TEN_SIZE, data.topTenSize);
		assertEquals(InitValues.DEFAULT_LONGEST_SIZE, data.longestSize);
		assertEquals("/monitor", data.reportingReqPath);
		assertFalse(data.traceFlag);
		assertEquals("10.11.12.13", data.ipAddress);
		assertEquals("hostname", data.hostname);
	}

}
