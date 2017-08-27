package com.citi.dde.common.exception;

public class PauseException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3134634760346670433L;

	public PauseException() {
		super();
	}

	public PauseException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public PauseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PauseException(String arg0) {
		super(arg0);
	}

	public PauseException(Throwable arg0) {
		super(arg0);
	}

}
