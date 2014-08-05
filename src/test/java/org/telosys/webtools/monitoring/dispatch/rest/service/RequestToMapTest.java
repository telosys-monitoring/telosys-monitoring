package org.telosys.webtools.monitoring.dispatch.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.Request;


public class RequestToMapTest {

	@Test
	public void testTransformRequestToMap() {
		// Given
		final RequestToMap requestToMap = new RequestToMap();

		final Request request = spy(new Request());
		request.countAllRequest = 100;
		request.countLongTimeRequests = 200;
		request.elapsedTime = 300;
		doReturn("request1").when(request).getURL();
		request.startTime = 400L;

		// When
		final Map<String, Object> map = requestToMap.transformRequestToMap(request );

		// Then
		assertEquals(100L, map.get("countAllRequest"));
		assertEquals(200L, map.get("countLongTimeRequests"));
		assertEquals(300L, map.get("elapsedTime"));
		assertEquals("request1", map.get("url"));
		assertEquals(400L, map.get("startTime"));
	}

}
