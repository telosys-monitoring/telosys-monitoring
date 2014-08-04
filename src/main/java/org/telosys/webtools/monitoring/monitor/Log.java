package org.telosys.webtools.monitoring.monitor;

import org.telosys.webtools.monitoring.bean.Request;

public class Log {

	private boolean traceFlag;

	public Log() {
		this.traceFlag = false;
	}

	public Log(final boolean traceFlag) {
		this.traceFlag = traceFlag;
	}

	/**
	 * @return the traceFlag
	 */
	public boolean getTraceFlag() {
		return traceFlag;
	}

	/**
	 * @param traceFlag the traceFlag to set
	 */
	public void setTraceFlag(final boolean traceFlag) {
		this.traceFlag = traceFlag;
	}

	/**
	 * Log the request in the output console.
	 * @param request Request.
	 */
	public final void trace(final Request request) {
		if ( traceFlag ) {
			trace( "Logging line : " + request);
		}
	}

	/**
	 * Log the message in the output console.
	 * @param msg Message
	 */
	public final void trace(final String msg) {
		if ( traceFlag ) {
			System.out.println("[TRACE] : " + msg );
		}
	}

	/**
	 * Manage the exception.
	 * @param throwable Error
	 */
	public void manageError(final Throwable throwable) {
		if(throwable == null) {
			return;
		}
		try {
			System.err.println("Error during monitoring : "+throwable.getClass().getName()+" : "+throwable.getMessage());
			throwable.printStackTrace(System.err);
		} catch(final Throwable throwable2) {
			// ignore this error
		}
	}

}
