package com.dh.common.restful;

import java.util.Map;

import com.dh.common.restful.entity.DHResponse;

/**
 * 
 *
 */
public interface DHRestful {
	
	/**
	 * restful调用
	 * @param appName 应用名
	 * @param jsonParam 请求数据
	 * @param serviceName service名
	 * @param methodName 方法名
	 * @param params 请求附加参数
	 * @return
	 */
	public DHResponse postForEntity(String appName, String jsonParam, String serviceName, String methodName, Map<String, String> params);
	
	/**
	 * restful调用
	 * @param appName 应用名
	 * @param jsonParam 请求数据
	 * @param serviceName service名
	 * @param methodName 方法名
	 * @return
	 */
	public DHResponse postForEntity(String appName, String jsonParam, String serviceName, String methodName);
	
	/**
	 * 查询全局参数
	 * @param paramKey
	 * @return
	 */
	String getParam(String paramKey);
}
