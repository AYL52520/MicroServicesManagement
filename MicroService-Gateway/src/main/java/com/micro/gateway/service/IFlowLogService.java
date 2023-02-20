package com.micro.gateway.service;

import com.micro.gateway.pojo.GatewayFlowLog;

/**
 * @Classname IFlowLogService
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/7 15:30
 * @Created by yangle
 */
public interface IFlowLogService {

    boolean insertFlow(GatewayFlowLog gatewayFlowLog);

}
