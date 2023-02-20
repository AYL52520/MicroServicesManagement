package com.micro.gateway.config;

import com.alibaba.fastjson.JSONObject;

/**
 * 定义接口行为 - 加载配置信息
 */
public interface Loadable {
	void load(JSONObject json);
}
