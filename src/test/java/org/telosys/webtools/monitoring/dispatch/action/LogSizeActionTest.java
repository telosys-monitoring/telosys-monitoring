package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.action.LogSizeAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class LogSizeActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "1");

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "1");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_param_not_a_number() throws Exception {
		// Given
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "aaa");
		data.logSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(200, data.logSize);
		assertEquals(200, data.logLines.getSize());
	}

	@Test
	public void testAction_logLines_null() throws Exception {
		// Given
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "100");
		data.logSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.logSize);
		assertEquals(100, data.logLines.getSize());
	}

	@Test
	public void testAction_logLines_copy() throws Exception {
		// Given
		final LogSizeAction action = new LogSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "100");
		data.logSize = 200;
		data.logLines = new CircularStack(200);
		final Request request1 = new Request();
		data.logLines.add(request1);

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.logSize);
		assertEquals(100, data.logLines.getSize());
		assertEquals(request1, data.logLines.getAllDescending().get(0));
	}

}
