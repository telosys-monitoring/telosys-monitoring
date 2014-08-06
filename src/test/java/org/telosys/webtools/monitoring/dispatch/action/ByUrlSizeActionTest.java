package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.action.ByUrlSizeAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class ByUrlSizeActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "1");

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "1");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_param_not_a_number() throws Exception {
		// Given
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "aaa");
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
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "100");
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
		final ByUrlSizeAction action = new ByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, "100");
		data.longestSize = 200;
		data.longestRequests = new LongestRequests(200);
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
