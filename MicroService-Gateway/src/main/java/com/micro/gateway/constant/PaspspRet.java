package com.micro.gateway.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

//错误码 - 应该提出至commin内
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaspspRet {
	OK("000000", "成功"),
	OUT_OF_WORKING("999998", "非工作时间"),
	ERROR("999999", "异常"),

	//网关的
	SDK_UPLOAD_ERROR("SDK001","文件上传时出现异常"),
	//TODO 扩展
	Applet_Error_Code("100000","小程序错误码以1开头"),
	PubAccount_Error_Code("200000","公众号错误码以2开头"),
	Proxy_Error_Code("300000","代理应用错误码以3开头"),
	Auth_Error_Code("400000","安全应用错误码以4开头"),
	Resource_Error_Code("500000","资源服务错误码以5开头");



	private final String code;
	private final String msg;
}
