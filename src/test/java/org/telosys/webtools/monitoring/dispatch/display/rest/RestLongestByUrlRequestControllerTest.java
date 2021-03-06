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
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.dispatch.display.rest.common.RequestToMap;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class RestLongestByUrlRequestControllerTest {

	@Test
	public void testMatch() throws Exception {
		// Given
		final RestLongestByUrlRequestController restTopRequestController = new RestLongestByUrlRequestController();

		// When/Then
		assertFalse(restTopRequestController.match(null, null));
		assertFalse(restTopRequestController.match(new String[] {}, null));
		assertFalse(restTopRequestController.match(new String[] {"longestByUrl"}, null));
		assertFalse(restTopRequestController.match(new String[] {"longestByUrl","after"}, null));
		assertTrue(restTopRequestController.match(new String[] {"rest","longestByUrl"}, null));
		assertFalse(restTopRequestController.match(new String[] {"rest","longestByUrl","after"}, null));
		assertFalse(restTopRequestController.match(new String[] {"rest","before","longestByUrl"}, null));
		assertFalse(restTopRequestController.match(new String[] {"rest","before","longestByUrl","after"}, null));
		assertFalse(restTopRequestController.match(new String[] {"rest","longestByUrl2"}, null));
	}

	@Test
	public void testGetData() throws Exception {
		// Given
		final RestLongestByUrlRequestController restTopRequestController = new RestLongestByUrlRequestController();

		final RequestToMap requestToMap = mock(RequestToMap.class);
		restTopRequestController.setRequestToMap(requestToMap);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {"longestByUrl"};
		final Map<String,String> params = new HashMap<String, String>();

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
		requestToMap.transformRequestToMap(request3);

		data.longestRequests = mock(TopRequests.class);
		when(data.longestRequests.getAllDescending()).thenReturn(requests);

		// When
		final Map<String, Object> map = restTopRequestController.getData(paths, params, data);

		// Then
		assertEquals(1, map.keySet().size());
		assertEquals(3, ((List<String>)map.get("longestByUrl")).size());
		assertEquals(mapRequest1, ((List<String>)map.get("longestByUrl")).get(0));
		assertEquals(mapRequest2, ((List<String>)map.get("longestByUrl")).get(1));
		assertEquals(mapRequest3, ((List<String>)map.get("longestByUrl")).get(2));

	}

}
