package com.micro.gateway.config;

import java.util.List;
import java.util.Map;

/**
 * 定义接口行为 转换为List
 */
public interface Listable {
	List<Object> toList();

}
