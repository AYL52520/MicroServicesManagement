package com.micro.service.config;

import java.util.List;

/**
 * 定义接口行为 转换为List
 */
public interface Listable {
	List<Object> toList();

}
