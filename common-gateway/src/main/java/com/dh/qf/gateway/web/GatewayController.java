package com.dh.qf.gateway.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dh.common.restful.DHRestful;
import com.dh.common.restful.entity.DHResponse;
import com.dh.common.utils.GsonUtil;
import com.dh.qf.enums.GWResponseEnum;
import com.dh.qf.enums.ParamKeyEnum;
/**
 * 
 * @author caisj
 *
 */
@RestController
public class GatewayController {

	private static Logger logger = LoggerFactory.getLogger(GatewayController.class);

	@Resource
	private DHRestful dhRestful;

	/**
	 * 调用接口
	 */
	@RequestMapping(value = "/api.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public String innerApi(HttpServletRequest request) {
		try {
			String appName = request.getParameter("appName");
			String serviceName = request.getParameter("serviceName");
			String methodName = request.getParameter("methodName");
			String jsonData = request.getParameter("jsonData");
			logger.info("innerApi.htm请求参数：" + GsonUtil.toString(request.getParameterMap()));
			if (StringUtils.isEmpty(appName)) {
				logger.warn("appName不能为空");
				DHResponse response = new DHResponse(GWResponseEnum.GW0002.getRespCode(),
						GWResponseEnum.GW0002.getRespDesc());
				return GsonUtil.toString(response);
			}
			if (StringUtils.isEmpty(serviceName)) {
				logger.warn("serviceName不能为空");
				DHResponse response = new DHResponse(GWResponseEnum.GW0003.getRespCode(),
						GWResponseEnum.GW0003.getRespDesc());
				return GsonUtil.toString(response);
			}
			if (StringUtils.isEmpty(methodName)) {
				logger.warn("methodName不能为空");
				DHResponse response = new DHResponse(GWResponseEnum.GW0004.getRespCode(),
						GWResponseEnum.GW0004.getRespDesc());
				return GsonUtil.toString(response);
			}
			// 获取内外部服务列表白名单
			String outService = dhRestful.getParam(ParamKeyEnum.IN_SERVICE_LIST.getCode());
			String[] outServiceList = outService.split(",");
			// 内外部服务列表白名单校验
			boolean flag = true;
			for (String service : outServiceList) {
				if (appName.equals(service)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				logger.warn("白名单校验未通过");
				DHResponse response = new DHResponse(GWResponseEnum.GW0006.getRespCode(),
						GWResponseEnum.GW0006.getRespDesc());
				return GsonUtil.toString(response);
			}
			DHResponse resp = dhRestful.postForEntity(appName, jsonData, serviceName, methodName);
			String respParam = GsonUtil.toString(resp);
			logger.info("innerApi.htm调用后台服务成功，返回参数" + respParam);
			return respParam;
		} catch (Exception e) {
			logger.error("系统异常", e);
			DHResponse response = new DHResponse(GWResponseEnum.ERROR.getRespCode(),
					GWResponseEnum.ERROR.getRespDesc());
			return GsonUtil.toString(response);
		}
	}

	private static String getIPAddress(HttpServletRequest request) {
		String ip = null;

		// X-Forwarded-For：Squid 服务代理
		String ipAddresses = request.getHeader("X-Forwarded-For");
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// Proxy-Client-IP：apache 服务代理
			ipAddresses = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// WL-Proxy-Client-IP：weblogic 服务代理
			ipAddresses = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// HTTP_CLIENT_IP：有些代理服务器
			ipAddresses = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// X-Real-IP：nginx服务代理
			ipAddresses = request.getHeader("X-Real-IP");
		}

		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if (ipAddresses != null && ipAddresses.length() != 0) {
			ip = ipAddresses.split(",")[0];
		}

		// 还是不能获取到，最后再通过request.getRemoteAddr();获取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}