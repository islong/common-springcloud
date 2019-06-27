package com.dh.qf.enums;
/**
 * 参数pKey枚举
 */
public enum ParamKeyEnum {
	IN_SERVICE_LIST("IN_SERVICE_LIST","内部服务列表"),
	SEND_SMS_CHANNEL_INFO("SEND_SMS_CHANNEL_INFO","caisj发短信通道信息"),
	;
	
	
	private String code;
	private String desc;
	private ParamKeyEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	
	
	
}
