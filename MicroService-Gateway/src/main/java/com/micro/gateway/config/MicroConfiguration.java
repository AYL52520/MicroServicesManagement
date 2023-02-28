package com.micro.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @Classname MicroConfiguration
 * 定义 routeconfiguration下的配置信息
 * InitializingBean, ApplicationListener<RefreshEvent>  实现了这两个接口 就可以实施加载了 不知道为哈
 * @Version 1.0.0
 * @Date 2023/2/21 17:14
 * @Created by yangle
 */

@ConfigurationProperties(prefix = "routeconfiguration")
@Configuration
@RefreshScope
public class MicroConfiguration implements InitializingBean, ApplicationListener<RefreshEvent> {

    @Autowired
    private RouteConfiguration routeConfiguration;

    //服务路由
    @Getter
    @Setter
    public Map<String, RouteDefinition> route;
    /**
     * RouteConfiguration 构建路由对象
     * @return
     */
    @PostConstruct
    @RefreshScope
    public void createRouteConfiguration(){
        routeConfiguration.load(getRoute());
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void onApplicationEvent(RefreshEvent event) {

    }
}
