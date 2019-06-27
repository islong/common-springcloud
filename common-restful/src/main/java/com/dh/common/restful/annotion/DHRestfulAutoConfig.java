package com.dh.common.restful.annotion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.dh.common.restful.DHRestful;
import com.dh.common.restful.impl.DHRestfulImpl;

/**
 * 
 * @author caisj
 *
 */
public class DHRestfulAutoConfig {
	
	@Value("${rest.ReadTimeout}")
	private int readTimeout;
	@Value("${rest.ConnectTimeout}")
	private int connectionTimeout;
	
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
		if(readTimeout>0) {
			httpRequestFactory.setReadTimeout(readTimeout);
		}else {
			httpRequestFactory.setReadTimeout(300000);
		}
		if(connectionTimeout>0) {
			httpRequestFactory.setConnectTimeout(connectionTimeout);
		}else {
			httpRequestFactory.setConnectTimeout(10000);
		}
		RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(httpRequestFactory));
		return restTemplate;
	}
	
	@Bean
	public DHRestful nhopRestfulManagerBySpring() {
		return new DHRestfulImpl();
	}
	
}
