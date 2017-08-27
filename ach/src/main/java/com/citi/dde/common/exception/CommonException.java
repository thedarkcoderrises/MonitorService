package com.citi.dde.common.exception;

import org.springframework.util.StringUtils;

import com.citi.dde.common.util.DDEConstants;

public abstract class CommonException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8939736293243175331L;
	
	private String className;

	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonException(String message) {
		super(message);
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException() {
		super();
	}

	public CommonException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CommonException(String message, String clazzName) {
		super(message);
		if(!StringUtils.isEmpty(clazzName)){
			setClassName(clazzName);
		}
	}

	public CommonException(String message, String clazzName, Exception e) {
		super(message,e);
		if(!StringUtils.isEmpty(clazzName)){
			setClassName(clazzName);
		}else{
			setClassName(DDEConstants.EMPTY_STRING);
		}
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	
	
}
