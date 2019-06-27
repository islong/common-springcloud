package com.dh.common.restful.entity;

/**
 * 
 *
 */
public class DHResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6357178428424697849L;

	public static final String ERROR = "999999";

	public static final String SUCCESS = "000000";
	
	public static final String ACCEPT = "000001";

	private String respJson;

	private String retCode;

	private String retMsg;

	public DHResponse() {

	}

	public DHResponse(String retCode, String retMsg) {
		super();
		this.retCode = retCode;
		this.retMsg = retMsg;
	}

	public DHResponse(String respJson, String retCode, String retMsg) {
		super();
		this.respJson = respJson;
		this.retCode = retCode;
		this.retMsg = retMsg;
	}

	public String getRespJson() {
		return respJson;
	}

	public void setRespJson(String respJson) {
		this.respJson = respJson;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

}
