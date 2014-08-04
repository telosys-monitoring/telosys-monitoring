package org.telosys.webtools.monitoring.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.InetAddress;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.junit.Test;


public class MonitorInitValuesManagerTest {

	@Test
	public void testInitValues() throws Exception {

		// Given
		final MonitorInitValuesManager monitorWebXmlManager = new MonitorInitValuesManager();

		final FilterConfig filterConfig = mock(FilterConfig.class);

		// When
		final MonitorInitValues initValues = monitorWebXmlManager.initValues(filterConfig);

		// Then
		assertEquals(MonitorInitValues.DEFAULT_DURATION_THRESHOLD, initValues.durationThreshold);
		assertEquals(MonitorInitValues.DEFAULT_LOG_SIZE, initValues.logSize);
		assertEquals(MonitorInitValues.DEFAULT_TOP_TEN_SIZE, initValues.topTenSize);
		assertEquals(MonitorInitValues.DEFAULT_LONGEST_SIZE, initValues.longestSize);
		assertEquals("/monitor", initValues.reportingReqPath);
		assertFalse(initValues.traceFlag);
		assertTrue(initValues.activated);
	}

	@Test
	public void testInitValuesCustomized() throws ServletException {

		// Given
		final MonitorInitValuesManager monitorWebXmlManager = new MonitorInitValuesManager();

		final FilterConfig filterConfig = mock(FilterConfig.class);
		when(filterConfig.getInitParameter("duration")).thenReturn("200");
		when(filterConfig.getInitParameter("logsize")).thenReturn("300");
		when(filterConfig.getInitParameter("toptensize")).thenReturn("400");
		when(filterConfig.getInitParameter("longestsize")).thenReturn("500");
		when(filterConfig.getInitParameter("reporting")).thenReturn("/monitoring2");
		when(filterConfig.getInitParameter("trace")).thenReturn("true");
		when(filterConfig.getInitParameter("activated")).thenReturn("true");

		// When
		final MonitorInitValues initValues = monitorWebXmlManager.initValues(filterConfig);

		// Then
		assertEquals(200, initValues.durationThreshold);
		assertEquals(300, initValues.logSize);
		assertEquals(400, initValues.topTenSize);
		assertEquals(500, initValues.longestSize);
		assertEquals("/monitoring2", initValues.reportingReqPath);
		assertTrue(initValues.traceFlag);
		assertTrue(initValues.activated);
	}

	@Test
	public void testInitValuesCustomized2() throws ServletException {
		// Given
		final MonitorInitValuesManager monitorWebXmlManager = new MonitorInitValuesManager();

		final FilterConfig filterConfig = mock(FilterConfig.class);
		when(filterConfig.getInitParameter("trace")).thenReturn("false");
		when(filterConfig.getInitParameter("activated")).thenReturn("false");

		// When
		final MonitorInitValues initValues = monitorWebXmlManager.initValues(filterConfig);

		// Then
		assertFalse(initValues.traceFlag);
		assertFalse(initValues.activated);
	}


	@Test
	public void testReset() throws Exception {

		// Given
		final MonitorInitValuesManager monitorWebXmlManager = new MonitorInitValuesManager();
		monitorWebXmlManager.setUtils(spy(monitorWebXmlManager.getUtils()));

		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		initValues.durationThreshold = MonitorInitValues.DEFAULT_DURATION_THRESHOLD;
		initValues.logSize = MonitorInitValues.DEFAULT_LOG_SIZE;
		initValues.topTenSize = MonitorInitValues.DEFAULT_TOP_TEN_SIZE;
		initValues.longestSize = MonitorInitValues.DEFAULT_LONGEST_SIZE;
		initValues.reportingReqPath = "/monitor";
		initValues.traceFlag = true;
		initValues.activated = true;

		final InetAddress adrLocale = mock(InetAddress.class);
		doReturn(adrLocale).when(monitorWebXmlManager.getUtils()).getLocalHost();
		when(adrLocale.getHostAddress()).thenReturn("10.11.12.13");
		when(adrLocale.getHostName()).thenReturn("hostname");

		// When
		monitorWebXmlManager.reset(initValues, data);

		// Then
		assertEquals(MonitorInitValues.DEFAULT_DURATION_THRESHOLD, data.durationThreshold);
		assertEquals(MonitorInitValues.DEFAULT_LOG_SIZE, data.logSize);
		assertEquals(MonitorInitValues.DEFAULT_LOG_SIZE, data.logLines.getSize());
		assertEquals(MonitorInitValues.DEFAULT_TOP_TEN_SIZE, data.topTenSize);
		assertEquals(MonitorInitValues.DEFAULT_TOP_TEN_SIZE, data.topRequests.getSize());
		assertEquals(MonitorInitValues.DEFAULT_LONGEST_SIZE, data.longestSize);
		assertEquals(MonitorInitValues.DEFAULT_LONGEST_SIZE, data.longestRequests.getSize());
		assertEquals("/monitor", data.reportingReqPath);
		assertEquals("10.11.12.13", data.ipAddress);
		assertEquals("hostname", data.hostname);
		assertTrue(data.traceFlag);
		assertTrue(data.activated);
	}

	@Test
	public void testResetCustomized() throws ServletException {

		// Given
		final MonitorInitValuesManager monitorWebXmlManager = new MonitorInitValuesManager();
		monitorWebXmlManager.setUtils(spy(monitorWebXmlManager.getUtils()));

		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		initValues.durationThreshold = 200;
		initValues.logSize = 300;
		initValues.topTenSize = 400;
		initValues.longestSize = 500;
		initValues.reportingReqPath = "/monitoring2";
		initValues.traceFlag = false;
		initValues.activated = false;

		final InetAddress adrLocale = mock(InetAddress.class);
		doReturn(adrLocale).when(monitorWebXmlManager.getUtils()).getLocalHost();
		when(adrLocale.getHostAddress()).thenReturn("10.11.12.13");
		when(adrLocale.getHostName()).thenReturn("hostname");

		// When
		monitorWebXmlManager.reset(initValues, data);

		// Then
		assertEquals(200, data.durationThreshold);
		assertEquals(300, data.logSize);
		assertEquals(300, data.logLines.getSize());
		assertEquals(400, data.topTenSize);
		assertEquals(400, data.topRequests.getSize());
		assertEquals(500, data.longestSize);
		assertEquals(500, data.longestRequests.getSize());
		assertEquals("/monitoring2", data.reportingReqPath);
		assertEquals("10.11.12.13", data.ipAddress);
		assertEquals("hostname", data.hostname);
		assertFalse(data.traceFlag);
		assertFalse(data.activated);
	}

}
