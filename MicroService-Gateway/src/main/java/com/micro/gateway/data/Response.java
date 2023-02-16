package com.micro.gateway.data;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.micro.gateway.constant.PaspspRet;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
		head.setCode(PaspspRet.OK.getCode());
		head.setMsg(PaspspRet.OK.getMsg());
		return response;
	}

	public static Response ok(JSONObject body) {
		Response response = ok();
		response.setBody(body);
		return response;
	}

	public static Response error(PaspspRet ret) {
		Response response = new Response();
		ResponseHead head = response.getHead();
		head.setCode(ret.getCode());
		head.setMsg(ret.getMsg());
		return response;
	}

	public static Response error(PaspspRet ret, String msg) {
		Response response = new Response();
		ResponseHead head = response.getHead();
		head.setCode(ret.getCode());
		head.setMsg(msg);
		return response;
	}

	public static Response error(PaspspRet ret, JSONObject body) {
		Response response = Response.error(ret);
		response.setBody(body);
		return response;
	}

	public static Response error(PaspspRet ret, String msg, JSONObject body) {
		Response response = new Response();
		ResponseHead head = response.getHead();
		head.setCode(ret.getCode());
		head.setMsg(msg);
		response.setBody(body);
		return response;
	}

	public boolean success() {
		return PaspspRet.OK.getCode().equals(getHead().getCode());
	}

	public boolean error() {
		return PaspspRet.ERROR.getCode().equals(getHead().getCode());
	}
}
