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
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class RestLongestServiceTest {

	@Test
	public void testMatch() throws Exception {
		// Given
		final RestLongestService restLongestsService = new RestLongestService();

		// When/Then
		assertFalse(restLongestsService.match(null, null));
		assertFalse(restLongestsService.match(new String[] {}, null));
		assertFalse(restLongestsService.match(new String[] {"longest"}, null));
		assertFalse(restLongestsService.match(new String[] {"longest","after"}, null));
		assertTrue(restLongestsService.match(new String[] {"rest","longest"}, null));
		assertTrue(restLongestsService.match(new String[] {"rest","longest","after"}, null));
		assertFalse(restLongestsService.match(new String[] {"rest","before","longest"}, null));
		assertFalse(restLongestsService.match(new String[] {"rest","before","longest","after"}, null));
		assertFalse(restLongestsService.match(new String[] {"rest","longest2"}, null));
	}

	@Test
	public void testGetData() throws Exception {
		// Given
		final RestLongestService restLongestsService = new RestLongestService();

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
		final Request request2 = mock(Request.class);
		requests.add(request2);
		when(request2.toString()).thenReturn("request2");

		data.longestRequests = mock(LongestRequests.class);
		when(data.longestRequests.getAllDescending()).thenReturn(requests);

		// When
		final Map<String, Object> map = restLongestsService.getData(paths, params, data);

		// Then
		assertEquals(1, map.keySet().size());
		assertEquals(2, ((List<String>)map.get("log")).size());
		assertEquals("request1", ((List<String>)map.get("log")).get(0));
		assertEquals("request2", ((List<String>)map.get("log")).get(1));

	}

}
