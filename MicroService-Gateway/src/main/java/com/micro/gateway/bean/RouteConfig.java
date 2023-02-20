package com.micro.gateway.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname RouteConfig
 * 监听consul配置
 * @Version 1.0.0
 * @Date 2023/2/20 16:35
 * @Created by yangle
 */
@ConfigurationProperties(prefix = "one")
@Configuration
public class RouteConfig {
}
