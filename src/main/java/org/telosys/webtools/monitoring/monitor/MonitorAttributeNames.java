package org.telosys.webtools.monitoring.monitor;

public interface MonitorAttributeNames {

	/** Action */
	String ATTRIBUTE_NAME_ACTION = "action";
	/** Action : Clean all logs */
	String ATTRIBUTE_VALUE_ACTION_CLEAR = "clear";
	/** Action : Reset values as defined in the web.xml */
	String ATTRIBUTE_VALUE_ACTION_RESET = "reset";
	/** Action : Stop monitoring */
	String ATTRIBUTE_VALUE_ACTION_STOP = "stop";
	/** Action : Start monitoring */
	String ATTRIBUTE_VALUE_ACTION_START = "start";

	/** Execution time threshold */
	String ATTRIBUTE_NAME_DURATION_THRESHOLD = "duration";
	/** Number of last stored requests */
	String ATTRIBUTE_NAME_LOG_SIZE           = "log_size" ;
	/** Number of top longest requests */
	String ATTRIBUTE_NAME_BY_TIME_SIZE       = "by_time_size" ;
	/** Number of longest requests */
	String ATTRIBUTE_NAME_BY_URL_SIZE       = "by_url_size" ;
	/** Indicates if information are displayed in the output console of the server */
	String ATTRIBUTE_NAME_TRACE_FLAG         = "trace" ;

	/** Activates storage of URL parameters */
	String ATTRIBUTE_NAME_URL_PARAMS_ACTIVATED      = "url_params_activated" ;
	/** Filter URL parameters */
	String ATTRIBUTE_NAME_URL_PARAMS_FILTER         = "url_params_filter" ;
	/** Show empty URL parameters */
	String ATTRIBUTE_NAME_URL_PARAMS_EMPTY         = "url_params_empty" ;

	/** Auto Refresh */
	String ATTRIBUTE_NAME_AUTO_REFRESH         = "auto_refresh_activated" ;
	/** Start Auto Refresh */
	String ATTRIBUTE_VALUE_AUTO_REFRESH_START = "true";
	/** Stop Auto Refresh */
	String ATTRIBUTE_VALUE_AUTO_REFRESH_STOP = "false";

}
