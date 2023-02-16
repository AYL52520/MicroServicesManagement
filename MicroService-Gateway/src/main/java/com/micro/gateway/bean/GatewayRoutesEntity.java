package com.micro.gateway.bean;

import lombok.Data;

/**
 * 基于配置实现路由
 */
@Data
public class GatewayRoutesEntity {

    private Long id;

    private String serviceId;

    private String uri;

    private String predicates;

    private String filters;

}
