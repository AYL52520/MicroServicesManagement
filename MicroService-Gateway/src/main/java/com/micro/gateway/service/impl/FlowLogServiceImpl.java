package com.micro.gateway.service.impl;

import com.micro.gateway.pojo.GatewayFlowLog;
import com.micro.gateway.service.IFlowLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @Classname FlowLogServiceImpl
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/7 15:32
 * @Created by yangle
 */
@Service
@Log4j2
public class FlowLogServiceImpl implements IFlowLogService {
    @Override
    public boolean insertFlow(GatewayFlowLog gatewayFlowLog) {
       log.info("FlowLogServiceImpl----存储日志信息");
       log.info(gatewayFlowLog.toString());
        return true;
    }
}
