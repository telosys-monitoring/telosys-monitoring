package org.telosys.webtools.monitoring.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Init values.
 */
public class MonitorInitValues implements Serializable {

	/**
	 * Serial id.
	 */
	public static final long serialVersionUID = -7397690120823673187L;

	/** Execution time threshold */
	public final static int DEFAULT_DURATION_THRESHOLD  = 1000 ; // 1 second
	/** Number of last stored requests */
	public final static int DEFAULT_LOG_SIZE            =  100 ;
	/** Number of top longest requests */
	public final static int DEFAULT_TOP_TEN_SIZE        =  10 ;
	/** Number of longest requests */
	public final static int DEFAULT_LONGEST_SIZE        =  10 ;
	/** Activate storage of URL params */
	public static final boolean DEFAULT_URL_PARAMS_ACTIVATED = false;

	/** Execution time threshold */
	public int     durationThreshold     = DEFAULT_DURATION_THRESHOLD ;
	/** Number of last stored requests */
	public int     logSize               = DEFAULT_LOG_SIZE ;
	/** Number of top longest requests */
	public int     topTenSize            = DEFAULT_TOP_TEN_SIZE ;
	/** Number of longest requests */
	public int     longestSize          = DEFAULT_LONGEST_SIZE ;

	/** URL path to the monitor reporting */
	public String  reportingReqPath      = "/monitor" ;
	/** Indicates if information are displayed in the output console of the server */
	public boolean traceFlag             = false ;
	/** Indicates if the filter get activated */
	public boolean activated             = true ;

	/** Count all requests */
	public long   countAllRequest           = 0 ;
	/** Count longest requests */
	public long   countLongTimeRequests     = 0 ;
	/** Count all requests for request log */
	public long   countAllRequestForRequest = 0 ;
	/** Indicates if URL parameters are stored */
	public Boolean urlParamsActivated = false;
	/** Indicates if URL parameters are stored */
	public List<String> urlParamsFilter = new ArrayList<String>();

}
