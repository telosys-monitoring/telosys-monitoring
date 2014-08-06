package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.bean.TopRequests;
import org.telosys.webtools.monitoring.dispatch.action.ByTimeSizeAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class ByTimeSizeActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "1");

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "1");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_param_not_a_number() throws Exception {
		// Given
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "aaa");
		data.topTenSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(200, data.topTenSize);
		assertEquals(200, data.topRequests.getSize());
	}

	@Test
	public void testAction_topRequests_null() throws Exception {
		// Given
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "100");
		data.topTenSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.topTenSize);
		assertEquals(100, data.topRequests.getSize());
	}

	@Test
	public void testAction_topRequests_copy() throws Exception {
		// Given
		final ByTimeSizeAction action = new ByTimeSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, "100");
		data.topTenSize = 200;
		data.topRequests = new TopRequests(200);
		final Request request1 = new Request();
		data.topRequests.add(request1);

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.topTenSize);
		assertEquals(100, data.topRequests.getSize());
		assertEquals(request1, data.topRequests.getAllDescending().get(0));
	}

}
