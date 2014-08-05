package org.telosys.webtools.monitoring.dispatch.rest.service.info;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

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
		assertFalse(restInfoService.match(null));
		assertFalse(restInfoService.match(new String[] {}));
		assertFalse(restInfoService.match(new String[] {"info","after"}));
		assertFalse(restInfoService.match(new String[] {"before","info"}));
		assertFalse(restInfoService.match(new String[] {"before","info","after"}));
		assertFalse(restInfoService.match(new String[] {"info2"}));
		assertTrue(restInfoService.match(new String[] {"info"}));
	}

	@Test
	public void testProcess() throws Exception {
		// Given
		final RestInfoService restInfoService = new RestInfoService();

		final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		final MonitorData data = mock(MonitorData.class);
		final MonitorInitValues initValues = new MonitorInitValues();

		// When
		restInfoService.process(httpServletRequest, httpServletResponse, data, initValues);

		// Then

	}

}
