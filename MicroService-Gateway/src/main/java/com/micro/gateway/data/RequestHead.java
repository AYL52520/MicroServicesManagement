package com.micro.gateway.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestHead {
	@JsonProperty(value = "URI", required = true)
	@JSONField(name = "URI")
	private String uri;

	@JsonProperty(value = "METHOD", required = true)
	@JSONField(name = "METHOD")
	private String method;

	@JsonProperty(value = "jwId", required = true)
	@JSONField(name = "jwId")
	private String jwId;


	@JsonProperty(value = "MID", required = true)
	@JSONField(name = "MID")
	private String mid;
}
