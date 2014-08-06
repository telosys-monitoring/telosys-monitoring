package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.dispatch.action.StopAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;


public class StopActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final StopAction action = new StopAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final StopAction action = new StopAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final StopAction action = new StopAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final StopAction action = new StopAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_STOP);

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final StopAction action = new StopAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_START);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction_false() throws Exception {
		// Given
		final StopAction action = new StopAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		data.activated = false;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(Boolean.FALSE, data.activated);
	}

	@Test
	public void testAction_true() throws Exception {
		// Given
		final StopAction action = new StopAction();

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		data.activated = true;

		// When
		action.action(params, data, initValues);

		// Then
		assertEquals(Boolean.FALSE, data.activated);
	}

}
