package com.dh.qf.enums;

public enum GWResponseEnum {
	SUCCESS("000000","成功"),
	ACTSUCC("000001","受理成功"),
	GW0002("GW0002","appName不能为空"),
	GW0003("GW0003","serviceName不能为空"),
	GW0004("GW0004","methodName不能为空"),
	GW0005("GW0005","sign不能为空"),
	GW0006("GW0006","白名单校验未通过"),
	GW0007("GW0007","验证签名失败"),
	GW0008("GW0008","ip白名单校验失败"),
	ERROR("999999","系统异常");
	private String respCode;
	private String respDesc;
	private GWResponseEnum(String respCode, String respDesc) {
		this.respCode = respCode;
		this.respDesc = respDesc;
	}
	public String getRespCode() {
		return respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
}
