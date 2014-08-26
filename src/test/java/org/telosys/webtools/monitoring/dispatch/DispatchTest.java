package org.telosys.webtools.monitoring.dispatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.dispatch.action.Action;
import org.telosys.webtools.monitoring.dispatch.action.ResetAction;
import org.telosys.webtools.monitoring.dispatch.display.Controller;
import org.telosys.webtools.monitoring.dispatch.parameter.GetParameters;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.monitor.MonitorInitValuesManager;
import org.telosys.webtools.monitoring.util.Utils;

public class DispatchTest {

	@Test
	public void testGetPaths() throws Exception {
		// Given
		final Dispatch dispatch = new Dispatch();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		final MonitorData data = new MonitorData();
		data.reportingReqPath = "/monitor";

		final String url = "/monitor/rest/path1/path2";
		when(httpServletRequest.getRequestURI()).thenReturn(url);

		// When
		final String[] paths = dispatch.getPaths(httpServletRequest, data);

		// Then
		assertEquals(3, paths.length);
		assertEquals("rest", paths[0]);
		assertEquals("path1", paths[1]);
		assertEquals("path2", paths[2]);
	}

	@Test
	public void testGetPaths_null() throws Exception {
		// Given
		final Dispatch dispatch = new Dispatch();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		final MonitorData data = new MonitorData();
		data.reportingReqPath = "/monitor";

		when(httpServletRequest.getRequestURI()).thenReturn(null);

		// When
		final String[] paths = dispatch.getPaths(httpServletRequest, data);

		// Then
		assertEquals(0, paths.length);
	}

	@Test
	public void testGetPaths_empty() throws Exception {
		// Given
		final Dispatch dispatch = new Dispatch();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		final MonitorData data = new MonitorData();
		data.reportingReqPath = "/monitor";

		when(httpServletRequest.getRequestURI()).thenReturn("");

		// When
		final String[] paths = dispatch.getPaths(httpServletRequest, data);

		// Then
		assertEquals(0, paths.length);
	}

	@Test
	public void testDispatch() {
		// Given
		final Dispatch dispatch = spy(new Dispatch());

		final GetParameters getParameters = mock(GetParameters.class);
		dispatch.setGetParameters(getParameters);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {
				"info"
		};
		doReturn(paths).when(dispatch).getPaths(httpServletRequest, data);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);

		final Controller controller = mock(Controller.class);
		when(controller.match(paths, params)).thenReturn(true);

		final List<Controller> controllers = new ArrayList<Controller>();
		controllers.add(controller);
		dispatch.setControllers(controllers);

		// When
		dispatch.dispatch(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(controller).process(httpServletRequest, httpServletResponse, paths, params, data, initValues);
		verify(httpServletResponse, never()).setStatus(404);
	}

	@Test
	public void testDispatch_None() {
		// Given
		final Dispatch dispatch = spy(new Dispatch());

		final GetParameters getParameters = mock(GetParameters.class);
		dispatch.setGetParameters(getParameters);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {
				"info"
		};
		doReturn(paths).when(dispatch).getPaths(httpServletRequest, data);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);

		final Controller controller = mock(Controller.class);
		when(controller.match(paths, params)).thenReturn(false);

		final List<Controller> controllers = new ArrayList<Controller>();
		controllers.add(controller);
		dispatch.setControllers(controllers);

		// When
		dispatch.dispatch(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(httpServletResponse).setStatus(404);
		verify(controller, never()).process(httpServletRequest, httpServletResponse, paths, params, data, initValues);
	}

	@Test
	public void testDispatch_Actions() throws IOException {
		// Given
		final Dispatch dispatch = spy(new Dispatch());

		final GetParameters getParameters = mock(GetParameters.class);
		dispatch.setGetParameters(getParameters);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {
				"info"
		};
		doReturn(paths).when(dispatch).getPaths(httpServletRequest, data);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);
		params.put("action", "reset");

		final Action action = mock(Action.class);
		when(action.match(paths, params)).thenReturn(true);

		final List<Action> actions = new ArrayList<Action>();
		actions.add(action);
		dispatch.setActions(actions);

		final Controller controller = mock(Controller.class);
		when(controller.match(paths, params)).thenReturn(true);
		final List<Controller> controllers = new ArrayList<Controller>();
		controllers.add(controller);
		dispatch.setControllers(controllers);

		final String redirectURL = "/redirectURL";
		when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer(redirectURL));

		// When
		dispatch.dispatch(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(action).action(params, data, initValues);
		verify(httpServletResponse).sendRedirect(redirectURL);
		verify(controller, never()).process(httpServletRequest, httpServletResponse, paths, params, data, initValues);
		verify(httpServletResponse, never()).setStatus(404);
	}

	@Test
	public void testAction1() {
		// Given
		final Dispatch dispatch = new Dispatch();

		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

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

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		final String[] paths = new String[] {};

		final Map<String,String> params = new HashMap<String,String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD, "201");
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "301");
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "401");
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "501");
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG, "true");

		// When
		final boolean hasActions = dispatch.doActions(httpServletRequest, httpServletResponse, paths, params, data, initValues);

		// Then
		assertTrue(hasActions);
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
		final Dispatch dispatch = new Dispatch();

		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

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

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		final String[] paths = new String[] {};

		final Map<String,String> params = new HashMap<String,String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD, "200");
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "300");
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "400");
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "500");

		// When
		final boolean hasActions = dispatch.doActions(httpServletRequest, httpServletResponse, paths, params, data, initValues);

		// Then
		assertTrue(hasActions);
		assertEquals(200, data.durationThreshold);
		assertEquals(300, data.logSize);
		assertEquals(logLines, data.logLines);
		assertEquals(400, data.topTenSize);
		assertEquals(topRequests, data.topRequests);
		assertEquals(500, data.longestSize);
		assertEquals(longestRequests, data.longestRequests);
	}

	@Test
	public void testActionStop() {
		// Given
		final Dispatch dispatch = new Dispatch();

		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		final String[] paths = new String[] {};

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.activated = true;

		final Map<String,String> params = new HashMap<String,String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_STOP);

		// When
		final boolean hasActions = dispatch.doActions(httpServletRequest, httpServletResponse, paths, params, data, initValues);

		// Then
		assertTrue(hasActions);
		assertFalse(data.activated);
	}

	@Test
	public void testActionStart() {
		// Given
		final Dispatch dispatch = new Dispatch();

		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.activated = false;

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		final String[] paths = new String[] {};

		final Map<String,String> params = new HashMap<String,String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_START);

		// When
		final boolean hasActions = dispatch.doActions(httpServletRequest, httpServletResponse, paths, params, data, initValues);

		// Then
		assertTrue(data.activated);
	}

	@Test
	public void testActionClean() {
		// Given
		final Dispatch dispatch = new Dispatch();

		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		final CircularStack logLines = mock(CircularStack.class);
		final TopRequests topRequests = mock(TopRequests.class);
		final LongestRequests longestRequests = mock(LongestRequests.class);
		data.logLines = logLines;
		data.topRequests = topRequests;
		data.longestRequests = longestRequests;

		data.logSize = 300;
		data.topTenSize = 400;
		data.longestSize = 500;

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		final String[] paths = new String[] {};

		final Map<String,String> params = new HashMap<String,String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_CLEAR);

		// When
		final boolean hasActions = dispatch.doActions(httpServletRequest, httpServletResponse, paths, params, data, initValues);

		// Then
		assertTrue(hasActions);
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
		final MonitorInitValuesManager monitorInitValuesManager = new MonitorInitValuesManager();
		final Utils utils = spy(new Utils());
		monitorInitValuesManager.setUtils(utils);

		final InetAddress adrLocale = mock(InetAddress.class);
		doReturn(adrLocale).when(utils).getLocalHost();
		when(adrLocale.getHostAddress()).thenReturn("10.11.12.13");
		when(adrLocale.getHostName()).thenReturn("hostname");

		final FilterConfig filterConfig = mock(FilterConfig.class);

		// When
		final MonitorInitValues initValues = monitorInitValuesManager.initValues(filterConfig);
		final MonitorData data = new MonitorData();
		monitorInitValuesManager.reset(initValues, data);

		// Then
		assertEquals(MonitorInitValues.DEFAULT_DURATION_THRESHOLD, data.durationThreshold);
		assertEquals(MonitorInitValues.DEFAULT_LOG_SIZE, data.logSize);
		assertEquals(MonitorInitValues.DEFAULT_TOP_TEN_SIZE, data.topTenSize);
		assertEquals(MonitorInitValues.DEFAULT_LONGEST_SIZE, data.longestSize);
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
		final Dispatch dispatch = new Dispatch();

		ResetAction resetAction = null;
		for(final Action action : dispatch.getActions()) {
			if(action instanceof ResetAction) {
				resetAction = (ResetAction) action;
			}
		}
		resetAction.setMonitorInitValuesManager(monitorInitValuesManager);

		final Map<String,String> params = new HashMap<String,String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_RESET);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		final String[] paths = new String[] {};

		// When
		final boolean hasActions = dispatch.doActions(httpServletRequest, httpServletResponse, paths, params, data, initValues);

		// Then
		assertTrue(hasActions);
		assertEquals(MonitorInitValues.DEFAULT_DURATION_THRESHOLD, data.durationThreshold);
		assertEquals(MonitorInitValues.DEFAULT_LOG_SIZE, data.logSize);
		assertEquals(MonitorInitValues.DEFAULT_TOP_TEN_SIZE, data.topTenSize);
		assertEquals(MonitorInitValues.DEFAULT_LONGEST_SIZE, data.longestSize);
		assertEquals("/monitor", data.reportingReqPath);
		assertFalse(data.traceFlag);
		assertEquals("10.11.12.13", data.ipAddress);
		assertEquals("hostname", data.hostname);
	}

}
