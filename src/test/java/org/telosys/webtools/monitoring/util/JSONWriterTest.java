package org.telosys.webtools.monitoring.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class JSONWriterTest {

	@Test
	public void testWrite_null() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, null);

		// Then
		assertEquals("{\n}", out.toString());
	}

	@Test
	public void testWrite_empty() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n}", out.toString());
	}

	@Test
	public void testWrite_String() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", "value1");

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": \"value1\"\n}", out.toString());
	}

	@Test
	public void testWrite_Number() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", 1);

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": 1\n}", out.toString());
	}

	@Test
	public void testWrite_TwoValues() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", 1);
		map.put("key2", "2");

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": 1,\n\"key2\": \"2\"\n}", out.toString());
	}

	@Test
	public void testWrite_SubObject() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("key1", 1);
		map2.put("key2", "2");

		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("map2", map2);

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"map2\": {\n\"key1\": 1,\n\"key2\": \"2\"\n}\n}", out.toString());
	}

	@Test
	public void testWrite_Array_empty() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": [\n]\n}", out.toString());
	}

	@Test
	public void testWrite_Array_null() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);
		list.add(null);

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": [\nnull\n]\n}", out.toString());
	}

	@Test
	public void testWrite_Array_string() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);
		list.add("value1");

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": [\n\"value1\"\n]\n}", out.toString());
	}

	@Test
	public void testWrite_Array_number() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);
		list.add(1);

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": [\n1\n]\n}", out.toString());
	}

	@Test
	public void testWrite_Array_twoValues() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);
		list.add("value1");
		list.add(1);

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": [\n\"value1\",\n1\n]\n}", out.toString());
	}

	@Test
	public void testWrite_Array_subObject_empty() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);

		final Map<String, Object> map2 = new HashMap<String, Object>();
		list.add(map2);

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": [\n{\n}\n]\n}", out.toString());
	}

	@Test
	public void testWrite_Array_subObject_oneValue() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);

		final Map<String, Object> map2 = new HashMap<String, Object>();
		list.add(map2);
		map2.put("key21", "value21");

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key1\": [\n{\n\"key21\": \"value21\"\n}\n]\n}", out.toString());
	}

	@Test
	public void testWrite_Array_subObject_twoValues() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key1", list);

		final Map<String, Object> map2 = new HashMap<String, Object>();
		list.add(map2);
		map2.put("key21", "value21");
		map2.put("key22", "value22");

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		boolean equals = false;
		if(out.toString().equals("{\n\"key1\": [\n{\n\"key21\": \"value21\",\n\"key22\": \"value22\"\n}\n]\n}")) {
			equals = true;
		}
		if(out.toString().equals("{\n\"key1\": [\n{\n\"key22\": \"value22\",\n\"key21\": \"value21\"\n}\n]\n}")) {
			equals = true;
		}
		assertTrue(equals);
	}

	@Test
	public void testWrite_Array_subObject_manyValues() throws Exception {
		// Given
		final JSONWriter jsonWriter = new JSONWriter();

		final Map<String, Object> map = new HashMap<String, Object>();

		final List<Object> list = new ArrayList<Object>();
		map.put("key", list);

		final Map<String, Object> map2 = new HashMap<String, Object>();
		list.add("before");
		list.add(map2);
		map2.put("key21", "value21");
		list.add("after");

		final StringBuffer out = new StringBuffer();

		// When
		jsonWriter.write(out, map);

		// Then
		assertEquals("{\n\"key\": [\n\"before\",\n{\n\"key21\": \"value21\"\n},\n\"after\"\n]\n}", out.toString());
	}

}
