package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.dispatch.action.LongestSizeAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class LongestSizeActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE, "1");

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE, "1");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_param_not_a_number() throws Exception {
		// Given
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE, "aaa");
		data.longestSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(200, data.longestSize);
		assertEquals(200, data.longestRequests.getSize());
	}

	@Test
	public void testAction_topRequests_null() throws Exception {
		// Given
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE, "100");
		data.longestSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.longestSize);
		assertEquals(100, data.longestRequests.getSize());
	}

	@Test
	public void testAction_topRequests_copy() throws Exception {
		// Given
		final LongestSizeAction action = new LongestSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_SIZE, "100");
		data.longestSize = 200;
		data.longestRequests = new TopRequests(200);
		final Request request1 = new Request();
		data.longestRequests.add(request1);

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.longestSize);
		assertEquals(100, data.longestRequests.getSize());
		assertEquals(request1, data.longestRequests.getAllDescending().get(0));
	}

}
