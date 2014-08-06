package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.dispatch.action.CleanAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class CleanActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final CleanAction action = new CleanAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final CleanAction action = new CleanAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final CleanAction action = new CleanAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final CleanAction action = new CleanAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_CLEAR);

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final CleanAction action = new CleanAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_STOP);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_old_null() throws Exception {
		// Given
		final CleanAction action = new CleanAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		data.logSize = 100;
		data.topTenSize = 200;
		data.longestSize = 300;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.logLines.getSize());
		assertEquals(200, data.topRequests.getSize());
		assertEquals(300, data.longestRequests.getSize());
	}

	@Test
	public void testAction_old_new() throws Exception {
		// Given
		final CleanAction action = new CleanAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		data.logSize = 100;
		data.topTenSize = 200;
		data.longestSize = 300;

		data.logLines = new CircularStack(10);
		data.topRequests = new TopRequests(20);
		data.longestRequests = new LongestRequests(20);

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.logLines.getSize());
		assertEquals(200, data.topRequests.getSize());
		assertEquals(300, data.longestRequests.getSize());
	}

}
