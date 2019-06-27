package com.dh.common.restful.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.dh.common.restful.DHRestful;
import com.dh.common.restful.entity.DHRequest;
import com.dh.common.restful.entity.DHResponse;
import com.dh.common.restful.exception.DHException;

/**
 * 
 *
 */
@Scope("singleton")
public class DHRestfulImpl implements DHRestful {
	
	private static final int HTTP_SUCESS_STATUS = 200;
	
	@Autowired
	private RestTemplate restTemplate;

	
	public DHResponse postForEntity(String appName, String jsonParam, String serviceName, String methodName, Map<String, String> params) {
		StringBuffer sb = new StringBuffer("http://");
        sb.append(appName).append("/").append("handle");
		DHRequest nrequest = new DHRequest(jsonParam, serviceName, methodName, params);
		HttpEntity<DHRequest> httpEntity = new HttpEntity<DHRequest>(nrequest);
		ResponseEntity<DHResponse> respJson = restTemplate.postForEntity(sb.toString(), httpEntity, DHResponse.class);
		if(respJson.getStatusCodeValue()!=200) {
			throw new DHException("999999", "调用服务异常");
		}
		DHResponse response = respJson.getBody();
		return response;
	}

	
	public DHResponse postForEntity(String appName, String jsonParam, String serviceName, String methodName) {
		StringBuffer sb = new StringBuffer("http://");
        sb.append(appName).append("/").append("handle");
		DHRequest nrequest = new DHRequest(jsonParam, serviceName, methodName);
		HttpEntity<DHRequest> httpEntity = new HttpEntity<DHRequest>(nrequest);
		ResponseEntity<DHResponse> respJson = restTemplate.postForEntity(sb.toString(),httpEntity , DHResponse.class);
		if(respJson.getStatusCodeValue()!=200) {
			throw new DHException("999999", "调用服务异常");
		}
		DHResponse response =respJson.getBody();
		return response;
	}
	
	public String getParam(String paramKey) {
		StringBuffer sb = new StringBuffer("http://");
        sb.append("common-param").append("/").append("handle");
        if(StringUtils.isEmpty(paramKey)) {
        	return null;
        }
		DHRequest nrequest = new DHRequest(paramKey, "common-param", "handle");
		HttpEntity<DHRequest> httpEntity = new HttpEntity<DHRequest>(nrequest);
		ResponseEntity<DHResponse> respJson = restTemplate.postForEntity(sb.toString(), httpEntity, DHResponse.class);
		if(respJson.getStatusCodeValue()!=200) {
			throw new DHException("999999", "调用服务异常");
		}
		DHResponse response = respJson.getBody();
		String paramValue = null;
		if(response.getRetCode().equals("000000")) {
			paramValue = response.getRespJson();
		}
		return paramValue;
	}

	
}
