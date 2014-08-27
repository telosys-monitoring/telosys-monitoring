package org.telosys.webtools.monitoring.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.telosys.webtools.monitoring.bean.CircularStack;
import org.telosys.webtools.monitoring.bean.LongestRequests;
import org.telosys.webtools.monitoring.bean.TopRequests;

public class MonitorData implements Serializable {

	/**
	 * Serial id.
	 */
	public static final long serialVersionUID = 4068606133994705317L;

	/** Execution time threshold */
	public int     durationThreshold;
	/** Number of last stored requests */
	public int     logSize;
	/** Number of top longest requests */
	public int     topTenSize;
	/** Number of longest requests */
	public int     longestSize;

	/** URL path to the monitor reporting */
	public String  reportingReqPath;
	/** Indicates if information are displayed in the output console of the server */
	public boolean traceFlag;
	/** Indicates if the filter is activated */
	public boolean activated;

	/** Indicates if URL parameters are stored */
	public boolean urlParamsActivated = false;
	/** Indicates if URL parameters are stored */
	public List<String> urlParamsFilter = new ArrayList<String>();
	/** Show empty parameters */
	public boolean urlParamsEmpty = false;


	/** Initialization date */
	public String initializationDate;
	/** Count all requests */
	public long   countAllRequest;
	/** Count longest requests */
	public long   countLongTimeRequests;
	/** Count all requests for request log */
	public long   countAllRequestForRequest;

	/** Last stored requests */
	public CircularStack logLines;
	/** Top longest requests */
	public TopRequests topRequests;
	/** Longest requests */
	public LongestRequests longestRequests;

	/** IP address */
	public String ipAddress;
	/** Host name */
	public String hostname;

	/** Auto refresh */
	public boolean autoRefreshActivated;

}
