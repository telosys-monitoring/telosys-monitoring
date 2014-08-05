package org.telosys.webtools.monitoring.dispatch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.telosys.webtools.monitoring.dispatch.action.Action;
import org.telosys.webtools.monitoring.dispatch.parameter.GetParameters;
import org.telosys.webtools.monitoring.dispatch.reporting.Reporting;
import org.telosys.webtools.monitoring.dispatch.reporting.html.HtmlReporting;
import org.telosys.webtools.monitoring.dispatch.rest.RestManager;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.util.Log;


public class DispatchTest {

	@Test
	public void testDispatch_RestURL() throws Exception {
		// Given
		final GetParameters getParameters = mock(GetParameters.class);
		final Action action = mock(Action.class);
		final Reporting reporting = mock(HtmlReporting.class);
		final Log log = mock(Log.class);
		final RestManager restManager = mock(RestManager.class);

		final Dispatch dispatch = new Dispatch();
		dispatch.getParameters = getParameters;
		dispatch.action = action;
		dispatch.reporting = reporting;
		dispatch.log = log;
		dispatch.restManager = restManager;

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = mock(MonitorInitValues.class);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);

		final boolean isMakingAction = false;
		when(action.action(params, data, initValues)).thenReturn(isMakingAction);

		final boolean isRestURL = true;
		when(restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues)).thenReturn(isRestURL);

		final String redirectURL = "/redirectURL";
		when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer(redirectURL));

		// When
		dispatch.dispatch(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(restManager).process(httpServletRequest, httpServletResponse, data, initValues);
		verify(reporting, never()).reporting(httpServletResponse, data);
		verify(httpServletResponse, never()).sendRedirect(redirectURL);
	}

	@Test
	public void testDispatch_withoutAction() throws Exception {
		// Given
		final GetParameters getParameters = mock(GetParameters.class);
		final Action action = mock(Action.class);
		final Reporting reporting = mock(HtmlReporting.class);
		final Log log = mock(Log.class);
		final RestManager restManager = mock(RestManager.class);

		final Dispatch dispatch = new Dispatch();
		dispatch.getParameters = getParameters;
		dispatch.action = action;
		dispatch.reporting = reporting;
		dispatch.log = log;
		dispatch.restManager = restManager;

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = mock(MonitorInitValues.class);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);

		final boolean isMakingAction = false;
		when(action.action(params, data, initValues)).thenReturn(isMakingAction);

		final boolean isRestURL = false;
		when(restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues)).thenReturn(isRestURL);

		final String redirectURL = "/redirectURL";
		when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer(redirectURL));

		// When
		dispatch.dispatch(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(reporting).reporting(httpServletResponse, data);
		verify(httpServletResponse, never()).sendRedirect(redirectURL);
		verify(restManager, never()).process(httpServletRequest, httpServletResponse, data, initValues);
	}

	@Test
	public void testDispatch_withAction() throws Exception {
		// Given
		final GetParameters getParameters = mock(GetParameters.class);
		final Action action = mock(Action.class);
		final Reporting reporting = mock(HtmlReporting.class);
		final Log log = mock(Log.class);
		final RestManager restManager = mock(RestManager.class);

		final Dispatch dispatch = new Dispatch();
		dispatch.getParameters = getParameters;
		dispatch.action = action;
		dispatch.reporting = reporting;
		dispatch.log = log;
		dispatch.restManager = restManager;

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = mock(MonitorInitValues.class);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);

		final boolean isMakingAction = true;
		when(action.action(params, data, initValues)).thenReturn(isMakingAction);

		final boolean isRestURL = false;
		when(restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues)).thenReturn(isRestURL);

		final String redirectURL = "/redirectURL";
		when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer(redirectURL));

		// When
		dispatch.dispatch(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(httpServletResponse).sendRedirect(redirectURL);
		verify(reporting, never()).reporting(httpServletResponse, data);
		verify(restManager, never()).process(httpServletRequest, httpServletResponse, data, initValues);
	}

}
