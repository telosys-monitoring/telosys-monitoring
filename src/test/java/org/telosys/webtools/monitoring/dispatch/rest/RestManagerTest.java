package org.telosys.webtools.monitoring.dispatch.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.telosys.webtools.monitoring.dispatch.parameter.GetParameters;
import org.telosys.webtools.monitoring.dispatch.rest.service.RestService;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;

public class RestManagerTest {

	@Test
	public void testIsRestURL_null() {
		// Given
		final RestManager restManager = new RestManager();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		data.reportingReqPath = "root/";

		when(httpServletRequest.getServletPath()).thenReturn(null);

		// When
		final boolean isRestURL = restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		assertFalse(isRestURL);
	}

	@Test
	public void testIsRestURL_false() {
		// Given
		final RestManager restManager = new RestManager();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		data.reportingReqPath = "root/";

		when(httpServletRequest.getServletPath()).thenReturn("toto");

		// When
		final boolean isRestURL = restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		assertFalse(isRestURL);
	}

	@Test
	public void testIsRestURL_true() {
		// Given
		final RestManager restManager = new RestManager();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		data.reportingReqPath = "root";
		final String url = data.reportingReqPath + RestManager.REST_PATH_URL;

		when(httpServletRequest.getServletPath()).thenReturn(url);

		// When
		final boolean isRestURL = restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		assertTrue(isRestURL);
	}

	@Test
	public void testIsRestURL_true2() {
		// Given
		final RestManager restManager = new RestManager();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		data.reportingReqPath = "root/";
		final String url = "root" + RestManager.REST_PATH_URL;

		when(httpServletRequest.getServletPath()).thenReturn(url);

		// When
		final boolean isRestURL = restManager.isRestURL(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		assertTrue(isRestURL);
	}

	@Test
	public void testGetPaths() throws Exception {
		// Given
		final RestManager restManager = new RestManager();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		final MonitorData data = new MonitorData();
		data.reportingReqPath = "/monitor";

		final String url = "/monitor/rest/path1/path2";
		when(httpServletRequest.getServletPath()).thenReturn(url);

		// When
		final String[] paths = restManager.getPaths(httpServletRequest, data);

		// Then
		assertEquals(3, paths.length);
		assertEquals("rest", paths[0]);
		assertEquals("path1", paths[1]);
		assertEquals("path2", paths[2]);
	}

	@Test
	public void testGetPaths_null() throws Exception {
		// Given
		final RestManager restManager = new RestManager();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		final MonitorData data = new MonitorData();
		data.reportingReqPath = "/monitor";

		when(httpServletRequest.getServletPath()).thenReturn(null);

		// When
		final String[] paths = restManager.getPaths(httpServletRequest, data);

		// Then
		assertEquals(0, paths.length);
	}

	@Test
	public void testGetPaths_empty() throws Exception {
		// Given
		final RestManager restManager = new RestManager();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		final MonitorData data = new MonitorData();
		data.reportingReqPath = "/monitor";

		when(httpServletRequest.getServletPath()).thenReturn("");

		// When
		final String[] paths = restManager.getPaths(httpServletRequest, data);

		// Then
		assertEquals(0, paths.length);
	}

	@Test
	public void testProcess() {
		// Given
		final RestManager restManager = spy(new RestManager());

		final GetParameters getParameters = mock(GetParameters.class);
		restManager.setGetParameters(getParameters);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {
				"info"
		};
		doReturn(paths).when(restManager).getPaths(httpServletRequest, data);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);

		final RestService restService = mock(RestService.class);
		when(restService.match(paths, params)).thenReturn(true);

		final List<RestService> restServices = new ArrayList<RestService>();
		restServices.add(restService);
		restManager.setRestServices(restServices);

		// When
		restManager.process(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(restService).process(httpServletRequest, httpServletResponse, paths, params, data, initValues);
		verify(httpServletResponse, never()).setStatus(404);
	}

	@Test
	public void testProcess_None() {
		// Given
		final RestManager restManager = spy(new RestManager());

		final GetParameters getParameters = mock(GetParameters.class);
		restManager.setGetParameters(getParameters);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {
				"info"
		};
		doReturn(paths).when(restManager).getPaths(httpServletRequest, data);

		final Map<String,String> params = new HashMap<String, String>();
		when(getParameters.getParameters(httpServletRequest)).thenReturn(params);

		final RestService restService = mock(RestService.class);
		when(restService.match(paths, params)).thenReturn(false);

		final List<RestService> restServices = new ArrayList<RestService>();
		restServices.add(restService);
		restManager.setRestServices(restServices);

		// When
		restManager.process(httpServletRequest, httpServletResponse, data, initValues);

		// Then
		verify(httpServletResponse).setStatus(404);
		verify(restService, never()).process(httpServletRequest, httpServletResponse, paths, params, data, initValues);
	}

}
