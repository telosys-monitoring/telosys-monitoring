package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.action.LatestAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class LatestActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE, "1");

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE, "1");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_param_not_a_number() throws Exception {
		// Given
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE, "aaa");
		data.latestSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(200, data.latestSize);
		assertEquals(200, data.latestLines.getSize());
	}

	@Test
	public void testAction_logLines_null() throws Exception {
		// Given
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE, "100");
		data.latestSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.latestSize);
		assertEquals(100, data.latestLines.getSize());
	}

	@Test
	public void testAction_logLines_copy() throws Exception {
		// Given
		final LatestAction action = new LatestAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE, "100");
		data.latestSize = 200;
		data.latestLines = new CircularStack(200);
		final Request request1 = new Request();
		data.latestLines.add(request1);

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.latestSize);
		assertEquals(100, data.latestLines.getSize());
		assertEquals(request1, data.latestLines.getAllDescending().get(0));
	}

}
