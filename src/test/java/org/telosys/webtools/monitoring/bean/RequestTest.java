package org.telosys.webtools.monitoring.bean;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;


public class RequestTest {

	@Test
	public void testToStringUrlParameters_1() {
		final Request request = new Request();

		final Map<String, String> urlParams = new HashMap<String,String>();
		request.urlParams = urlParams;

		final boolean urlParamsEmpty = true;

		// When
		final String result = request.toStringUrlParameters(urlParamsEmpty);

		assertEquals("", result);
	}

	@Test
	public void testToStringUrlParameters_2() {
		final Request request = new Request();

		final Map<String, String> urlParams = new TreeMap<String, String>();
		request.urlParams = urlParams;

		urlParams.put("key1", "value1");
		urlParams.put("key2", null);
		urlParams.put("key3", "");
		urlParams.put("key4", " ");
		urlParams.put("key5", " value5  ");

		final boolean urlParamsEmpty = true;

		// When
		final String result = request.toStringUrlParameters(urlParamsEmpty);

		assertEquals(" - URL params : [ key1 = \"value1\", key2 = , key3 = \"\", key4 = \" \", key5 = \" value5  \" ]", result);
	}

	@Test
	public void testToStringUrlParameters_3() {
		final Request request = new Request();

		final Map<String, String> urlParams = new TreeMap<String, String>();
		request.urlParams = urlParams;

		urlParams.put("key1", "value1");
		urlParams.put("key2", null);
		urlParams.put("key3", "");
		urlParams.put("key4", " ");
		urlParams.put("key5", " value5  ");

		final boolean urlParamsEmpty = false;

		// When
		final String result = request.toStringUrlParameters(urlParamsEmpty);

		assertEquals(" - URL params : [ key1 = \"value1\", key5 = \" value5  \" ]", result);
	}

}
