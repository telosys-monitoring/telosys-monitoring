package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.dispatch.action.LongestByUrlSizeAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class LongestByUrlSizeActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE, "1");

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LATEST_SIZE, "1");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_param_not_a_number() throws Exception {
		// Given
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE, "aaa");
		data.longestByUrlTempSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(200, data.longestByUrlTempSize);
		assertEquals(200, data.longestByUrlTempRequests.getSize());
	}

	@Test
	public void testAction_topRequests_null() throws Exception {
		// Given
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE, "100");
		data.longestByUrlTempSize = 200;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.longestByUrlTempSize);
		assertEquals(100, data.longestByUrlTempRequests.getSize());
	}

	@Test
	public void testAction_topRequests_copy() throws Exception {
		// Given
		final LongestByUrlSizeAction action = new LongestByUrlSizeAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LONGEST_BY_URL_SIZE, "100");
		data.longestByUrlTempSize = 200;
		data.longestByUrlTempRequests = new LongestRequests(200);
		final Request request1 = new Request();
		data.longestByUrlTempRequests.add(request1);

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.longestByUrlTempSize);
		assertEquals(100, data.longestByUrlTempRequests.getSize());
		assertEquals(request1, data.longestByUrlTempRequests.getAllDescending().get(0));
	}

}
