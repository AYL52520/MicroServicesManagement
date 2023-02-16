package com.micro.gateway.data;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 请求对象
 * @auth yangle
 */
@Data
public class Request {
	@JsonProperty(value = "HEAD", required = true)
	@JSONField(name = "HEAD")
	private RequestHead head = new RequestHead();

	@JsonProperty(value = "BODY", required = true)
	@JSONField(name = "BODY")
	private JSONObject body = new JSONObject();
}
