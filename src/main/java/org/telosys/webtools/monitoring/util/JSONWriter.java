package org.telosys.webtools.monitoring.util;

import java.util.List;
import java.util.Map;

/**
 * Write in JSON format.
 */
public class JSONWriter {

	/**
	 * Write JSON.
	 * @param out Output
	 * @param map Data
	 */
	@SuppressWarnings("unchecked")
	public void write(final StringBuffer out, final Map<String,Object> map) {
		if(map == null) {
			out.append("{\n}");
		} else {
			out.append("{");
			boolean isFirst = true;
			for(final String key : map.keySet()) {
				if(isFirst) {
					isFirst = false;
					out.append("\n");
				} else {
					out.append(",\n");
				}
				if(map.get(key) instanceof Map) {
					out.append("\"").append(key).append("\": ");
					write(out, (Map<String, Object>) map.get(key));
				} else if(map.get(key) instanceof List) {
					out.append("\"").append(key).append("\": ");
					writeList(out, (List<Object>) map.get(key));
				} else {
					addKeyValue(out, key, map.get(key));
				}
			}
			out.append("\n}");
		}
	}

	/**
	 * Write list of values.
	 * @param out JSON content
	 * @param list List of values
	 */
	@SuppressWarnings("unchecked")
	private void writeList(final StringBuffer out, final List<Object> list) {
		if(list == null) {
			return;
		}
		out.append("[");
		boolean isFirst = true;
		for(final Object value : list) {
			if(isFirst) {
				isFirst = false;
				out.append("\n");
			} else {
				out.append(",\n");
			}
			if(value instanceof Map) {
				write(out, (Map<String, Object>) value);
			} else if(value instanceof List) {
				writeList(out, (List<Object>) value);
			} else {
				addValue(out, value);
			}
		}
		out.append("\n]");
	}

	/**
	 * Add key and value.
	 * @param out Output string value
	 * @param key Key
	 * @param value Value
	 */
	public void addKeyValue(final StringBuffer out, final String key, final Object value) {
		if(value == null) {
			out.append("\"").append(key).append("\": null");
		}
		else if(value instanceof Number) {
			out.append("\"").append(key).append("\": ").append(value);
		}
		else {
			out.append("\"").append(key).append("\": \"").append(value).append("\"");
		}
	}

	/**
	 * Add key and value.
	 * @param out Output string value
	 * @param key Key
	 * @param value Value
	 */
	public void addValue(final StringBuffer out, final Object value) {
		if(value == null) {
			out.append("null");
		}
		else if(value instanceof Number) {
			out.append(value);
		}
		else {
			out.append("\"").append(value).append("\"");
		}
	}

}
