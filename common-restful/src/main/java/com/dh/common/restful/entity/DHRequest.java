package com.dh.common.restful.entity;

import java.util.Map;

/**
 * 
 *
 */
public class DHRequest implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8474627009012230779L;

	private String jsonParam;

	private String reqService;

	private String reqMethod;

	private Map<String, String> params;

	public DHRequest() {

	}

	public DHRequest(String reqService, String reqMethod) {
		super();
		this.reqService = reqService;
		this.reqMethod = reqMethod;
	}

	public DHRequest(String jsonParam, String reqService, String reqMethod) {
		super();
		this.jsonParam = jsonParam;
		this.reqService = reqService;
		this.reqMethod = reqMethod;
	}

	public DHRequest(String jsonParam, String reqService, String reqMethod, Map<String, String> params) {
		super();
		this.jsonParam = jsonParam;
		this.reqService = reqService;
		this.reqMethod = reqMethod;
		this.params = params;
	}

	public String getJsonParam() {
		return jsonParam;
	}

	public void setJsonParam(String jsonParam) {
		this.jsonParam = jsonParam;
	}

	public String getReqService() {
		return reqService;
	}

	public void setReqService(String reqService) {
		this.reqService = reqService;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

}
