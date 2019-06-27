package com.dh.common.restful.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dh.common.restful.entity.DHRequest;
import com.dh.common.restful.entity.DHResponse;
import com.dh.common.restful.exception.DHException;
import com.dh.common.utils.GsonUtil;

/**
 * 
 * @param <T>
 */
@RestController
public class CommonController<T> {
	
	private Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private ApplicationContext applicationConetxt;
	
	@Autowired  
    MyHealthChecker myHealthChecker;
	
	/**
	 * 修改服务状态
	 * @param status true：up，false：down
	 * */
	@RequestMapping("/updateStatus")  
    public String up(@RequestParam("status") Boolean status) {  
        myHealthChecker.setUp(status);  
  
        return status.toString();  
    }  
	
	@RequestMapping(value = "/infom")
	public String info(String body) {
		logger.info("body"+body);
		return "success";
		
	}
	
	@RequestMapping(value = "/handle")
	public DHResponse handle(HttpEntity<DHRequest> entity) {
		DHRequest request = entity.getBody();
		logger.info("请求接口 "+request.getReqService()+"=>"+request.getReqMethod()+"参数:"+GsonUtil.toString(request));
		try {
			validate(request);
		} catch (DHException e1) {
			return new DHResponse(String.valueOf(e1.getErrorCode()), e1.getErrorMsg());
		}
		
		String serviceName = request.getReqService();
		String reqMethod = request.getReqMethod();
		Object object = applicationConetxt.getBean(serviceName);
		if(object == null) {
			return new DHResponse(DHResponse.ERROR, " do not find service bean: " + serviceName);
		}
		try {
			Method method = object.getClass().getMethod(reqMethod, DHRequest.class);
			DHResponse response = (DHResponse) method.invoke(object, request);
			String respParam = GsonUtil.toString(response);
			logger.info("返回"+request.getReqService()+"=>"+request.getReqMethod()+"参数:"+respParam);
			return response;
		} catch (NoSuchMethodException e) {
			logger.error("  request invoke happen error. ", e);
			return new DHResponse(DHResponse.ERROR,  (serviceName +" : NoSuchMethodException"));
		} catch (SecurityException e) {
			logger.error("  request invoke happen error. ", e);
			return new DHResponse(DHResponse.ERROR,  (serviceName +" : SecurityException"));
		} catch (IllegalAccessException e) {
			logger.error("  request invoke happen error. ", e);
			return new DHResponse(DHResponse.ERROR,  (serviceName +" : IllegalAccessException"));
		} catch (IllegalArgumentException e) {
			logger.error("  request invoke happen error. ", e);
			return new DHResponse(DHResponse.ERROR,  (serviceName +" : IllegalArgumentException"));
		} catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof DHException){
				DHException bizEx = (DHException) e.getTargetException();
				logger.error("  request invoke biz service happen error. ", bizEx);
				return new DHResponse(bizEx.getErrorCode(),  bizEx.getErrorMsg());
			}
			logger.error("  request invoke happen error. ", e);
			return new DHResponse(DHResponse.ERROR,  (serviceName +" : InvocationTargetException"));
		} catch (Exception e) {
			if(e instanceof DHException){
				DHException bizEx = (DHException) e;
				return new DHResponse(bizEx.getErrorCode(),  bizEx.getErrorMsg());
			}
			return new DHResponse(DHResponse.ERROR,  (serviceName +" : InvocationTargetException"));
		}
	 }
	
	private void validate(DHRequest request) throws DHException {
		String serviceName = request.getReqService();
		String reqMethod = request.getReqMethod();
		String emsg = null;
		if(StringUtils.isEmpty(serviceName)) {
			logger.error(" request donot set invoke service name ");
			emsg = " request donot set invoke service name ";
			throw new DHException(DHResponse.ERROR, emsg);
		}
		if(StringUtils.isEmpty(reqMethod)) {
			logger.error(" request donot set invoke service method ");
			emsg = " request donot set invoke service method ";
			throw new DHException(DHResponse.ERROR, emsg);
		}
		
	}
	
}
