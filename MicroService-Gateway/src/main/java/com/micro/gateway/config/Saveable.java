package com.micro.gateway.config;

import com.alibaba.fastjson.JSONObject;

/**
 * 接口信息 - 预留存储信息
 */
public interface Saveable {
	JSONObject save();
}
