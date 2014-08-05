package org.telosys.webtools.monitoring.util;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;


public class JSONReaderTest {

	@Test
	public void testRead_null() throws Exception {
		// Given
		final JSONReader reader = new JSONReader();

		final String json = null;

		// When
		final Map<String, Object> data = reader.read(json);

		// Then
		assertEquals(0, data.size());
	}

	@Test
	public void testRead_empty() throws Exception {
		// Given
		final JSONReader reader = new JSONReader();

		final String json = "";

		// When
		final Map<String, Object> data = reader.read(json);

		// Then
		assertEquals(0, data.size());
	}

	@Test
	public void testRead_objectEmpty() throws Exception {
		// Given
		final JSONReader reader = new JSONReader();

		final String json = "{}";

		// When
		final Map<String, Object> data = reader.read(json);

		// Then
		assertEquals(0, data.size());
	}

	@Test
	public void testRead_objectEmpty2() throws Exception {
		// Given
		final JSONReader reader = new JSONReader();

		final String json = "\n{\n}\n";

		// When
		final Map<String, Object> data = reader.read(json);

		// Then
		assertEquals(0, data.size());
	}

	@Test
	public void testRead_string() throws Exception {
		// Given
		final JSONReader reader = new JSONReader();

		final String json = "\n{\n\"key1\": \"value1\"\n}\n";

		// When
		final Map<String, Object> data = reader.read(json);

		// Then
		assertEquals(1, data.size());
		assertEquals("value1", "key1");
	}

}
