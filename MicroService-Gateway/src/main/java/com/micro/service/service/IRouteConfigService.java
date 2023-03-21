package com.micro.service.service;

import com.micro.service.pojo.RouteConfig;

/**
 * @Classname IRouteConfigService
 * @Description 路由类型服务类
 * @Version 1.0.0
 * @Date 2023/1/31 20:55
 * @Created by yangle
 */
public interface IRouteConfigService {

    RouteConfig selectByMidAndJwid(String mid , String jwid);

}
