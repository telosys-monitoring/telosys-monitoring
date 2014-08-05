package org.telosys.webtools.monitoring.dispatch.rest.service.info;

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
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class RestInfoServiceTest {

	@Test
	public void testMatch() throws Exception {
		// Given
		final RestInfoService restInfoService = new RestInfoService();

		// When/Then
		assertFalse(restInfoService.match(null, null));
		assertFalse(restInfoService.match(new String[] {}, null));
		assertFalse(restInfoService.match(new String[] {"info","after"}, null));
		assertFalse(restInfoService.match(new String[] {"before","info"}, null));
		assertFalse(restInfoService.match(new String[] {"before","info","after"}, null));
		assertFalse(restInfoService.match(new String[] {"info2"}, null));
		assertTrue(restInfoService.match(new String[] {"info"}, null));
	}

	@Test
	public void testProcess() throws Exception {
		// Given
		final RestInfoService restInfoService = new RestInfoService();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = new MonitorData();
		final MonitorInitValues initValues = new MonitorInitValues();

		final String[] paths = new String[] {"info"};
		final Map<String,String> params = new HashMap<String, String>();

		data.ipAddress = "1.2.3.4";
		data.hostname = "hostname";
		data.durationThreshold = 100;
		data.logSize = 200;
		data.topTenSize = 300;
		data.longestSize = 400;
		data.initializationDate = "initializationDate";
		data.countAllRequest = 500;
		data.countLongTimeRequests = 600;

		// When
		final Map<String, Object> json = restInfoService.getData(paths, params, data);

		// Then
		assertEquals(3, json.keySet().size());

		final Map<String, Object> host = (Map<String, Object>) json.get("host");

		assertEquals("1.2.3.4", host.get("ipAdress"));
		assertEquals("hostname",host.get("hostname"));
		assertNotNull(host.get("java.version"));
		assertNotNull(host.get("java.vendor"));
		assertNotNull(host.get("os.arch"));
		assertNotNull(host.get("os.name"));
		assertNotNull(host.get("os.version"));

		final Map<String, Object> configuration = (Map<String, Object>) json.get("configuration");

		assertEquals(100,configuration.get("durationThreshold"));
		assertEquals(200,configuration.get("logSize"));
		assertEquals(300,configuration.get("topTenSize"));
		assertEquals(400,configuration.get("longestSize"));

		final Map<String, Object> monitoring = (Map<String, Object>) json.get("monitoring");

		assertEquals("initializationDate",monitoring.get("initializationDate"));
		assertEquals(500L,monitoring.get("countAllRequest"));
		assertEquals(600L,monitoring.get("countLongTimeRequests"));

	}

}