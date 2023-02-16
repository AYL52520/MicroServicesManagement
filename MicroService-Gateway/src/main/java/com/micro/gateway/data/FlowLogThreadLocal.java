package com.micro.gateway.data;

import com.micro.gateway.bean.GatewayFlowLog;

/**
 * @Classname FlowLogThreadLocal
 * 创建链接的上下文对象 在上下文中使用
 * @Version 1.0.0
 * @Date 2023/2/7 14:39
 * @Created by yangle
 */
public class FlowLogThreadLocal {

    private static ThreadLocal<GatewayFlowLog> flowLogThreadLocal =
            new ThreadLocal<>();

    public static GatewayFlowLog getFlowLog() {
        return flowLogThreadLocal.get();
    }

    public static void setFlowLog(GatewayFlowLog flowLog) {
        flowLogThreadLocal.set(flowLog);
    }

    //清除对象引用
    public static void remove(){
        flowLogThreadLocal.remove();
    }
}
