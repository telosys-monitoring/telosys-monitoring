package org.telosys.webtools.monitoring.dispatch.display.web;

import java.io.PrintWriter;

import org.telosys.webtools.monitoring.monitor.MonitorData;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;

public class ActionBarForReporting {

	/**
	 * Add action bar Javascript.
	 * @param out Output
	 */
	public void addActionBarJS(final PrintWriter out) {
		out.println("<script>");
		out.println("function doRefresh(){document.location=document.location;}");
		out.println("function doAction(action){document.location=document.location+'?action='+action;}");
		out.println("function doParam(key,value){if(key==null||key==''||value==null||value==''){return;}document.location=document.location+'?'+key+'='+value;}");
		out.println("</script>");
	}

	/**
	 * Add action bar.
	 * @param out Output
	 * @param data Monitor data
	 */
	public void addActionBar(final PrintWriter out, final MonitorData data) {
		out.println("<div class='actionbar'>");
		out.println("<div class='content'>");
		out.println("<input type='button' value='Refresh' onclick='doRefresh()'/>");
		out.println(" | ");
		out.println("<select id='key'>");
		out.println("<option value=''></option>");
		out.println("<option value='"+MonitorAttributeNames.ATTRIBUTE_NAME_DURATION_THRESHOLD+"'>Duration threshold</option>");
		out.println("<option value='"+MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE+"'>Log size</option>");
		out.println("<option value='"+MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE+"'>Top requests by Time size</option>");
		out.println("<option value='"+MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE+"'>Top requests by URL size</option>");
		out.println("</select>");
		out.println("<input type='text' id='value' value=''/>");
		out.println("<input type='button' value='Modify' onclick='doParam(document.getElementById(\"key\").value,document.getElementById(\"value\").value)'/>");
		out.println(" | ");
		out.println("<input type='button' value='Clear logs' onclick='doAction(\"clear\")'/>");
		out.println(" | ");
		out.println("<input type='button' value='Reset' onclick='doAction(\"reset\")'/>");
		out.println(" | ");
		if(data.activated) {
			out.println("<input type='button' value=' Stop ' onclick='doAction(\""+MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_STOP+"\")'/>");
		} else {
			out.println("<input type='button' value=' Start ' onclick='doAction(\""+MonitorAttributeNames.ATTRIBUTE_VALUE_ACTION_START+"\")'/>");
		}
		out.println("</div>");
		out.println("</div>");
	}

}