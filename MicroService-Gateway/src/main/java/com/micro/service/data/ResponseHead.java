package com.micro.service.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseHead {
	@JsonProperty("CODE")
	@JSONField(name = "CODE")
	private String code;

	@JsonProperty("MSG")
	@JSONField(name = "MSG")
	private String msg;

}
