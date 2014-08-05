package org.telosys.webtools.monitoring.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class UtilsTest {

	private Utils utils = new Utils();

	@Test
	public void testJoinURL1() throws Exception {

		// Given
		final String url = null;
		final String path = null;

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("", result);
	}

	@Test
	public void testJoinURL2() throws Exception {

		// Given
		final String url = "";
		final String path = null;

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("", result);
	}

	@Test
	public void testJoinURL3() throws Exception {

		// Given
		final String url = null;
		final String path = "";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("", result);
	}

	@Test
	public void testJoinURL4() throws Exception {

		// Given
		final String url = "";
		final String path = "";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("", result);
	}

	@Test
	public void testJoinURL5() throws Exception {

		// Given
		final String url = "/root";
		final String path = "";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/root", result);
	}

	@Test
	public void testJoinURL5b() throws Exception {

		// Given
		final String url = "/root/";
		final String path = "";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/root", result);
	}

	@Test
	public void testJoinURL6() throws Exception {

		// Given
		final String url = "/root";
		final String path = null;

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/root", result);
	}

	@Test
	public void testJoinURL6b() throws Exception {

		// Given
		final String url = "/root/";
		final String path = null;

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/root", result);
	}

	@Test
	public void testJoinURL7() throws Exception {

		// Given
		final String url = "/root";
		final String path = "/path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/root/path", result);
	}

	@Test
	public void testJoinURL8() throws Exception {

		// Given
		final String url = "/root/";
		final String path = "/path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/root/path", result);
	}

	@Test
	public void testJoinURL9() throws Exception {

		// Given
		final String url = "/";
		final String path = "/path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/path", result);
	}

	@Test
	public void testJoinURL10() throws Exception {

		// Given
		final String url = "/";
		final String path = "path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/path", result);
	}

	@Test
	public void testJoinURL11() throws Exception {

		// Given
		final String url = "";
		final String path = "/path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/path", result);
	}

	@Test
	public void testJoinURL12() throws Exception {

		// Given
		final String url = "";
		final String path = "path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/path", result);
	}

	@Test
	public void testJoinURL13() throws Exception {

		// Given
		final String url = null;
		final String path = "/path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/path", result);
	}

	@Test
	public void testJoinURL14() throws Exception {

		// Given
		final String url = null;
		final String path = "path";

		// When
		final String result = utils.joinURL(url, path);

		// Then
		assertEquals("/path", result);
	}

	@Test
	public void testSplitURLPaths_nullOrEmpty() throws Exception {
		assertEquals(0, utils.splitURLPaths(null).length);
		assertEquals(0, utils.splitURLPaths("").length);
		assertEquals(0, utils.splitURLPaths("/").length);
	}

	@Test
	public void testSplitURLPaths_path() throws Exception {
		// When
		final String[] paths = utils.splitURLPaths("/path");

		// Then
		assertEquals(1, paths.length);
		assertEquals("path", paths[0]);
	}

	@Test
	public void testSplitURLPaths_paths1() throws Exception {
		// When
		final String[] paths = utils.splitURLPaths("path1/path2");

		// Then
		assertEquals(2, paths.length);
		assertEquals("path1", paths[0]);
		assertEquals("path2", paths[1]);
	}

	@Test
	public void testSplitURLPaths_paths2() throws Exception {
		// When
		final String[] paths = utils.splitURLPaths("/path1/path2");

		// Then
		assertEquals(2, paths.length);
		assertEquals("path1", paths[0]);
		assertEquals("path2", paths[1]);
	}

	@Test
	public void testSplitURLPaths_paths3() throws Exception {
		// When
		final String[] paths = utils.splitURLPaths("path1/path2/");

		// Then
		assertEquals(2, paths.length);
		assertEquals("path1", paths[0]);
		assertEquals("path2", paths[1]);
	}

	@Test
	public void testSplitURLPaths_paths4() throws Exception {
		// When
		final String[] paths = utils.splitURLPaths("/path1/path2/");

		// Then
		assertEquals(2, paths.length);
		assertEquals("path1", paths[0]);
		assertEquals("path2", paths[1]);
	}

	@Test
	public void testRemoveRootURL() throws Exception {
		assertNull(utils.removeRootURL(null, null));
		assertNull(utils.removeRootURL(null, ""));
		assertNull(utils.removeRootURL(null, "/root"));
		assertEquals("", utils.removeRootURL("", "/root"));
		assertEquals("url", utils.removeRootURL("url", "/root"));
		assertEquals("url", utils.removeRootURL("/url", "/root"));
		assertEquals("", utils.removeRootURL("root", "/root"));
		assertEquals("", utils.removeRootURL("/root", "/root"));
		assertEquals("url", utils.removeRootURL("root/url", "/root"));
		assertEquals("url", utils.removeRootURL("/root/url", "/root"));
	}

	@Test
	public void testTrimToEmpty() throws Exception {
		assertEquals("", utils.trimToEmpty(null));
		assertEquals("", utils.trimToEmpty(""));
		assertEquals("", utils.trimToEmpty("  "));
		assertEquals("", utils.trimToEmpty("\n"));
		assertEquals("a", utils.trimToEmpty("   a   "));
		assertEquals("a", utils.trimToEmpty("   a  \n "));
	}

	@Test
	public void testTrimToNull() throws Exception {
		assertEquals(null, utils.trimToNull(null));
		assertEquals(null, utils.trimToNull(""));
		assertEquals(null, utils.trimToNull("  "));
		assertEquals(null, utils.trimToNull("\n"));
		assertEquals("a", utils.trimToNull("   a   "));
		assertEquals("a", utils.trimToNull("   a  \n "));
	}

}
