package com.citi.dde.common.exception;

public class MonitorException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4042989112082300405L;

	public MonitorException() {
		super();
	}

	public MonitorException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MonitorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MonitorException(String arg0) {
		super(arg0);
	}

	public MonitorException(Throwable arg0) {
		super(arg0);
	}

}
