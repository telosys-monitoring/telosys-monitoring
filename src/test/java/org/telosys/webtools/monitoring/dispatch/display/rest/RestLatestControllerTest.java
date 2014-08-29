package org.telosys.webtools.monitoring.dispatch.display.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.display.rest.RestLatestController;
import org.telosys.webtools.monitoring.dispatch.display.rest.common.RequestToMap;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class RestLatestControllerTest {

	@Test
	public void testMatch() throws Exception {
		// Given
		final RestLatestController restLogController = new RestLatestController();

		// When/Then
		assertFalse(restLogController.match(null, null));
		assertFalse(restLogController.match(new String[] {}, null));
		assertFalse(restLogController.match(new String[] {"log"}, null));
		assertFalse(restLogController.match(new String[] {"log","after"}, null));
		assertTrue(restLogController.match(new String[] {"rest","log"}, null));
		assertFalse(restLogController.match(new String[] {"rest","log","after"}, null));
		assertFalse(restLogController.match(new String[] {"rest","before","log"}, null));
		assertFalse(restLogController.match(new String[] {"rest","before","log","after"}, null));
		assertFalse(restLogController.match(new String[] {"rest","info2"}, null));
	}

	@Test
	public void testGetData() throws Exception {
		// Given
		final RestLatestController restLogController = new RestLatestController();

		final RequestToMap requestToMap = mock(RequestToMap.class);
		restLogController.setRequestToMap(requestToMap);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {"log"};
		final Map<String,String> params = new HashMap<String, String>();

		final List<Request> requests = new ArrayList<Request>();
		final Request request1 = mock(Request.class);
		requests.add(request1);
		when(request1.toString()).thenReturn("request1");
		final Map<String, Object> mapRequest1 = new HashMap<String, Object>();
		requestToMap.transformRequestToMap(request1);

		final Request request2 = mock(Request.class);
		requests.add(request2);
		when(request2.toString()).thenReturn("request2");
		final Map<String, Object> mapRequest2 = new HashMap<String, Object>();
		requestToMap.transformRequestToMap(request2);

		data.latestLines = mock(CircularStack.class);
		when(data.latestLines.getAllAscending()).thenReturn(requests);

		// When
		final Map<String, Object> map = restLogController.getData(paths, params, data);

		// Then
		assertEquals(1, map.keySet().size());
		assertEquals(2, ((List<String>)map.get("log")).size());
		assertEquals(mapRequest1, ((List<String>)map.get("log")).get(0));
		assertEquals(mapRequest2, ((List<String>)map.get("log")).get(1));

	}

	@Test
	public void testGetData_withStart() throws Exception {
		// Given
		final RestLatestController restLogController = new RestLatestController();

		final RequestToMap requestToMap = mock(RequestToMap.class);
		restLogController.setRequestToMap(requestToMap);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {"log"};
		final Map<String,String> params = new HashMap<String, String>();
		params.put("start", "2");

		final List<Request> requests = new ArrayList<Request>();

		final Request request1 = mock(Request.class);
		requests.add(request1);
		request1.countLongTimeRequests = 1L;
		final Map<String, Object> mapRequest1 = new HashMap<String, Object>();
		requestToMap.transformRequestToMap(request1);

		final Request request2 = mock(Request.class);
		requests.add(request2);
		request2.countLongTimeRequests = 2L;
		final Map<String, Object> mapRequest2 = new HashMap<String, Object>();
		requestToMap.transformRequestToMap(request2);

		final Request request3 = mock(Request.class);
		requests.add(request3);
		request3.countLongTimeRequests = 3L;
		final Map<String, Object> mapRequest3 = new HashMap<String, Object>();
		requestToMap.transformRequestToMap(request2);

		data.latestLines = mock(CircularStack.class);
		when(data.latestLines.getAllAscending()).thenReturn(requests);

		// When
		final Map<String, Object> map = restLogController.getData(paths, params, data);

		// Then
		assertEquals(1, map.keySet().size());
		assertEquals(2, ((List<String>)map.get("log")).size());
		assertEquals(mapRequest2, ((List<String>)map.get("log")).get(0));
		assertEquals(mapRequest3, ((List<String>)map.get("log")).get(1));

	}

}
