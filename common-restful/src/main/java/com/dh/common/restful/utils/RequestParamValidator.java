package com.dh.common.restful.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dh.common.restful.exception.DHException;

public class RequestParamValidator<T,R> {

	private Logger logger = LoggerFactory.getLogger(RequestParamValidator.class);
	
	public void validateParam(T param, R result) throws DHException  {
		try {
			ValidatorUtils.validate(param);
		} catch (IllegalArgumentException ie) {
			logger.error("参数校验未通过: "+ie);
			throw new DHException("999999", ValidatorUtils.buildErrorMessage(ie.getMessage()));
		} catch (Exception e) {
			logger.error("参数校验未通过: "+e);
			throw new DHException("999999", ValidatorUtils.buildErrorMessage(e.getMessage()));
		}
	}
}
