package org.telosys.webtools.monitoring.dispatch.display.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class RestInfoControllerTest {

	@Test
	public void testMatch() throws Exception {
		// Given
		final RestInfoController restInfoController = new RestInfoController();

		// When/Then
		assertFalse(restInfoController.match(null, null));
		assertFalse(restInfoController.match(new String[] {}, null));
		assertTrue(restInfoController.match(new String[] {"rest","info"}, null));
		assertFalse(restInfoController.match(new String[] {"info"}, null));
		assertFalse(restInfoController.match(new String[] {"rest","info","after"}, null));
		assertFalse(restInfoController.match(new String[] {"before","rest","info"}, null));
		assertFalse(restInfoController.match(new String[] {"rest","before","info","after"}, null));
		assertFalse(restInfoController.match(new String[] {"rest","info2"}, null));
	}

	@Test
	public void testProcess() throws Exception {
		// Given
		final RestInfoController restInfoController = new RestInfoController();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {"info"};
		final Map<String,String> params = new HashMap<String, String>();

		data.activated = true;
		data.ipAddress = "1.2.3.4";
		data.hostname = "hostname";
		data.durationThreshold = 100;
		data.latestSize = 200;
		data.longestSize = 300;
		data.longestByUrlTempSize = 400;
		data.initializationDate = "initializationDate";
		data.countAllRequests = 500;
		data.countLongTimeRequests = 600;

		// When
		final Map<String, Object> json = restInfoController.getData(paths, params, data);

		// Then
		assertEquals(3, json.keySet().size());

		final Map<String, Object> host = (Map<String, Object>) json.get("host");

		assertEquals("1.2.3.4", host.get("ip_adress"));
		assertEquals("hostname",host.get("hostname"));
		assertNotNull(host.get("java_version"));
		assertNotNull(host.get("java_vendor"));
		assertNotNull(host.get("os_arch"));
		assertNotNull(host.get("os_name"));
		assertNotNull(host.get("os_version"));

		final Map<String, Object> configuration = (Map<String, Object>) json.get("configuration");

		assertEquals(100,configuration.get("duration"));
		assertEquals(200,configuration.get(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE));
		assertEquals(300,configuration.get(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE));
		assertEquals(400,configuration.get(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE));

		final Map<String, Object> monitoring = (Map<String, Object>) json.get("monitoring");

		assertEquals(Boolean.TRUE, monitoring.get("activated"));
		assertEquals("initializationDate",monitoring.get("initialization_date"));
		assertEquals(500L,monitoring.get("count_all_requests"));
		assertEquals(600L,monitoring.get("count_long_time_requests"));

	}

}
