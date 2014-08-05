package org.telosys.webtools.monitoring.dispatch.rest.service.info;

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
import org.telosys.webtools.monitoring.dispatch.rest.service.RequestToMap;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class RestTopRequestsServiceTest {

	@Test
	public void testMatch() throws Exception {
		// Given
		final RestTopRequestsService restTopRequestsService = new RestTopRequestsService();

		// When/Then
		assertFalse(restTopRequestsService.match(null, null));
		assertFalse(restTopRequestsService.match(new String[] {}, null));
		assertFalse(restTopRequestsService.match(new String[] {"top"}, null));
		assertFalse(restTopRequestsService.match(new String[] {"top","after"}, null));
		assertTrue(restTopRequestsService.match(new String[] {"rest","top"}, null));
		assertTrue(restTopRequestsService.match(new String[] {"rest","top","after"}, null));
		assertFalse(restTopRequestsService.match(new String[] {"rest","before","top"}, null));
		assertFalse(restTopRequestsService.match(new String[] {"rest","before","top","after"}, null));
		assertFalse(restTopRequestsService.match(new String[] {"rest","top2"}, null));
	}

	@Test
	public void testGetData() throws Exception {
		// Given
		final RestTopRequestsService restTopRequestsService = new RestTopRequestsService();

		final RequestToMap requestToMap = mock(RequestToMap.class);
		restTopRequestsService.setRequestToMap(requestToMap);

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {"top"};
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
		requestToMap.transformRequestToMap(request3);

		data.topRequests = mock(TopRequests.class);
		when(data.topRequests.getAllDescending()).thenReturn(requests);

		// When
		final Map<String, Object> map = restTopRequestsService.getData(paths, params, data);

		// Then
		assertEquals(1, map.keySet().size());
		assertEquals(2, ((List<String>)map.get("top")).size());
		assertEquals(mapRequest2, ((List<String>)map.get("top")).get(0));
		assertEquals(mapRequest3, ((List<String>)map.get("top")).get(1));

	}

}
