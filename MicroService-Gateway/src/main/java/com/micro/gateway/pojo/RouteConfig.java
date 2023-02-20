package com.micro.gateway.pojo;

import lombok.Data;

/**
 * @Classname RouteConfig
 * 数据库方式实现
 * @Version 1.0.0
 * @Date 2023/1/31 21:04
 * @Created by yangle
 */
@Data
public class RouteConfig {

    private String mid; //选择键位

    private String jwId;

    private String url; //地址 http://ip:port

    private String CreateBy;
}
