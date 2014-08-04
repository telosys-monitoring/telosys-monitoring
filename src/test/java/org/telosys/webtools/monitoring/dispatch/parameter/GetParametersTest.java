package org.telosys.webtools.monitoring.dispatch.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.telosys.webtools.monitoring.RequestsMonitor;
import org.telosys.webtools.monitoring.monitor.RequestAttributeNames;


public class GetParametersTest {

	@Test
	public void testGetParameters() {
		// Given
		GetParameters getParameters = new GetParameters();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getQueryString()).thenReturn(
				RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD + "=201&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE + "=301&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE + "=401&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE + "=501&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG + "=true&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_ACTION + "="+RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_CLEAR);
		
		// When
		Map<String,String> params = getParameters.getParameters(request);
		
		// Then
		assertEquals("201", params.get(RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD));
		assertEquals("301", params.get(RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE));
		assertEquals("401", params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE));
		assertEquals("501", params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE));
		assertEquals("true", params.get(RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG));
		assertEquals(RequestAttributeNames.ATTRIBUTE_VALUE_ACTION_CLEAR, params.get(RequestAttributeNames.ATTRIBUTE_NAME_ACTION));
	}

	@Test
	public void testGetParameters2() {
		// Given
		GetParameters getParameters = new GetParameters();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getQueryString()).thenReturn(
				RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD + "=&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE + "=1&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE + "&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE + "&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG + "=" + "&"
				+ RequestAttributeNames.ATTRIBUTE_NAME_ACTION + "");
		
		// When
		Map<String,String> params = getParameters.getParameters(request);
		
		// Then
		assertNull(params.get(RequestAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD));
		assertEquals("1", params.get(RequestAttributeNames.ATTRIBUTE_NAME_LOG_SIZE));
		assertNull(params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE));
		assertNull(params.get(RequestAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE));
		assertNull(params.get(RequestAttributeNames.ATTRIBUTE_NAME_TRACE_FLAG));
		assertNull(params.get(RequestAttributeNames.ATTRIBUTE_NAME_ACTION));
	}
		
}
