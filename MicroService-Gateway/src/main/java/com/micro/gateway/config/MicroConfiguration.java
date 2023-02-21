package com.micro.gateway.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Classname MicroConfiguration
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/21 17:14
 * @Created by yangle
 */

@ConfigurationProperties(prefix = "one")
@Configuration
//@Component
@Setter //太坑了
public class MicroConfiguration {

//    @Value("{one.route}")
    public Map<String,String> route;
//    @Value("${one.test}")
    public String test;

    @Bean
    @RefreshScope
    public RouteConfiguration createRouteConfiguration(){
        RouteConfiguration rc = new RouteConfiguration();
        System.out.println("进来了"+route+test);
//        rc.load(JSONObject.parseObject(microGatewayConfig));
        return  rc;
    }

}
