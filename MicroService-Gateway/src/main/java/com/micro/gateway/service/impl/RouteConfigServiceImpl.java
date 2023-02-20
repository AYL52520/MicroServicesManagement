package com.micro.gateway.service.impl;

import com.micro.gateway.pojo.RouteConfig;
import com.micro.gateway.service.IRouteConfigService;
import org.springframework.stereotype.Service;

/**
 * @Classname IRouteConfigService
 * @Description 路由类型服务类
 * @Version 1.0.0
 * @Date 2023/1/31 20:55
 * @Created by yangle
 */
@Service
public class RouteConfigServiceImpl implements IRouteConfigService {


    @Override
    public RouteConfig selectByMidAndJwid(String mid, String jwid) {
        //根据mid 和 JWID查询
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setJwId(jwid);
        routeConfig.setMid(mid);
        routeConfig.setUrl("http://127.0.0.1:9090");
        return routeConfig;
    }
}
