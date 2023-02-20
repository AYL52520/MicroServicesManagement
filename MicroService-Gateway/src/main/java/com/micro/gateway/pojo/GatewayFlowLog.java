package com.micro.gateway.pojo;

import lombok.Data;

/**
 * @Classname GatewayFlowLog
 * 记录流水信息
 * @Version 1.0.0
 * @Date 2023/2/7 14:23
 * @Created by yangle
 */
@Data
public class GatewayFlowLog {
    //全局流水号
    private String flowId;
    private String appid;
    private String openId;
    private String routeUrl;
    private String dealStartTime;
    private String dealEndTime;
    private String routeReqTime;
    private String routeRespTime;
    private String channelId;
    private String routeType;
    private String dealState; //0：成功 1失败
    private String routeState;//0：成功 1失败
}
