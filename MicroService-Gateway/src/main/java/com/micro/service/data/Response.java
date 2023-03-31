package com.micro.service.data;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import yang.micro.exception.SDKExceptionEnums;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Response {
	@JsonProperty("HEAD")
	@JSONField(name = "HEAD")
	private ResponseHead head = new ResponseHead();

	@JsonProperty("BODY")
	@JSONField(name = "BODY")
	private JSONObject body = new JSONObject();

	public static Response ok() {
		Response response = new Response();
		ResponseHead head = response.getHead();
		head.setCode(SDKExceptionEnums.OK.getCode());
		head.setMsg(SDKExceptionEnums.OK.getMessage());
		return response;
	}

	public static Response ok(JSONObject body) {
		Response response = ok();
		response.setBody(body);
		return response;
	}

	public static Response error(SDKExceptionEnums ret) {
		Response response = new Response();
		ResponseHead head = response.getHead();
		head.setCode(ret.getCode());
		head.setMsg(ret.getMessage());
		return response;
	}

	public static Response error(SDKExceptionEnums ret, String msg) {
		Response response = new Response();
		ResponseHead head = response.getHead();
		head.setCode(ret.getCode());
		head.setMsg(msg);
		return response;
	}

	public static Response error(SDKExceptionEnums ret, JSONObject body) {
		Response response = Response.error(ret);
		response.setBody(body);
		return response;
	}

	public static Response error(SDKExceptionEnums ret, String msg, JSONObject body) {
		Response response = new Response();
		ResponseHead head = response.getHead();
		head.setCode(ret.getCode());
		head.setMsg(msg);
		response.setBody(body);
		return response;
	}

	public boolean success() {
		return SDKExceptionEnums.OK.getCode().equals(getHead().getCode());
	}

	public boolean error() {
		return SDKExceptionEnums.ERROR.getCode().equals(getHead().getCode());
	}
}
