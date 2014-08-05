package org.telosys.webtools.monitoring.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Write in JSON format.
 */
public class JSONReader {

	/**
	 * Utils.
	 */
	private Utils utils = new Utils();

	/**
	 * Read JSON content.
	 * @param json JSON content
	 * @return JSON data
	 */
	public Map<String,Object> read(String json) {
		final Map<String,Object> map = new HashMap<String,Object>();

		json = utils.trimToNull(json);
		if((json == null) || (json.length() == 0)) {
			return map;
		}

		if(json.charAt(0) == '{') {
			readObject(map, json);
		}

		return map;
	}

	protected void readObject(final Map<String,Object> map, final String json) {
		if((json == null) || (json.charAt(0) != '{')) {
			return;
		}
		if((json.length() < 2) || (json.charAt(json.length()-1) != '}')) {
			throw new IllegalStateException("Malformed JSON : missing ending symbol '}'");
		}
		final String jsonObject = utils.trimToNull(json.substring(1, json.length()));
		readKeyValues(map, jsonObject);
	}

	protected void readKeyValues(final Map<String,Object> map, final String json) {
		if(json == null) {
			return;
		}
		final String[] jsonKeyValues = utils.split(json, ',');
		for(final String jsonKeyValue : jsonKeyValues) {
			readKeyValue(map, jsonKeyValue);
		}
	}

	protected void readKeyValue(final Map<String,Object> map, final String json) {
		if(json == null) {
			return;
		}
		final String[] keyValue = utils.split(json, ':');
		map.put(keyValue[0], keyValue[1]);
	}

}
