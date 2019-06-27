package com.dh.common.restful.exception;

/**
 * 
 *
 */
public class DHException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7383215609458616301L;

	private Throwable t;
	
	private String errorCode;
	
	private String errorMsg;
	
	public DHException(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
		
		
	
	public DHException(Throwable t, String errorCode, String errorMsg) {
		this(errorCode, errorMsg);
		this.t = t;
	}



	public Throwable getT() {
		return t;
	}



	public void setT(Throwable t) {
		this.t = t;
	}



	public String getErrorCode() {
		return errorCode;
	}



	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public String getErrorMsg() {
		return errorMsg;
	}



	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
