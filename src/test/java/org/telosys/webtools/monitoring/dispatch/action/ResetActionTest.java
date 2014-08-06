package org.telosys.webtools.monitoring.dispatch.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.webtools.monitoring.dispatch.action.ResetAction;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;
import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.monitor.MonitorInitValuesManager;


public class ResetActionTest {

	@Test
	public void testMatch_noparam() throws Exception {
		final ResetAction action = new ResetAction();

		final Map<String, String> params = new HashMap<String, String>();

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_null() throws Exception {
		final ResetAction action = new ResetAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, null);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_empty() throws Exception {
		final ResetAction action = new ResetAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, "");

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testMatch_Ok() throws Exception {
		final ResetAction action = new ResetAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_RESET);

		// When
		assertTrue(action.match(null, params));
	}

	@Test
	public void testMatch_Ko() throws Exception {
		final ResetAction action = new ResetAction();

		final Map<String, String> params = new HashMap<String, String>();
		params.put(MonitorAttributeNames.ATTRIBUTE_NAME_ACTION, MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_STOP);

		// When
		assertFalse(action.match(null, params));
	}

	@Test
	public void testAction() throws Exception {
		// Given
		final ResetAction action = new ResetAction();
		action.monitorInitValuesManager = mock(MonitorInitValuesManager.class);

		final Map<String, String> params = new HashMap<String, String>();
		final MonitorInitValues initValues = new MonitorInitValues();
		final MonitorData data = new MonitorData();

		// When
		action.action(params, data, initValues);

		// Then
		verify(action.monitorInitValuesManager).reset(initValues, data);
	}

}
