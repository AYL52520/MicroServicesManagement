package com.micro.gateway.config;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.Map;

/**
 * 定义接口行为 - 加载配置信息
 */
public interface Loadable {
	void load(Map<String, RouteDefinition> loadMap);
}
