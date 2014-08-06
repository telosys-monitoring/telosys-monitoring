package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.dispatch.action.DurationThresholdAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class DurationThresholdActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD, "1");

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, "1");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_noparam() throws Exception {
		// Given
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		data.durationThreshold = 100;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.durationThreshold);
	}

	@Test
	public void testAction_param_ok() throws Exception {
		// Given
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		data.durationThreshold = 100;
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD, "500");

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(500, data.durationThreshold);
	}

	@Test
	public void testAction_param_ko() throws Exception {
		// Given
		final DurationThresholdAction action = new DurationThresholdAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		data.durationThreshold = 100;
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD, "aaa");

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(100, data.durationThreshold);
	}

}
