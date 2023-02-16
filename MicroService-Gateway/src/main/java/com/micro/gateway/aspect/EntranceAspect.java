package com.micro.gateway.aspect;

import com.micro.gateway.bean.GatewayFlowLog;
import com.micro.gateway.data.FlowLogThreadLocal;
import com.micro.gateway.data.Response;
import com.micro.gateway.service.IFlowLogService;
import com.micro.gateway.util.TimeUtil;
import com.micro.gateway.util.UidUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.UnknownHostException;
import java.time.Instant;

/**
 * @Classname EntranceAspect
 * @Description 请求入口记录日志/创建日志对象
 * @Version 1.0.0
 * @Date 2023/2/7 15:19
 * @Created by yangle
 */
@Aspect
@Component
@Log4j2
public class EntranceAspect {

    @Autowired
    private IFlowLogService flowLogService;

    @Pointcut("execution(public * com.micro.gateway.controller.EntranceController.*(..))")
    public void cut() {
    }

    //记录日志工作
    @Around("cut()")
    public Mono<Response> around(ProceedingJoinPoint point) {
        log.info("---> 开始进行日志记录");
        Mono<Response> proceed=null;
        GatewayFlowLog gatewayFlowLog = new GatewayFlowLog();
        //进行解密操作  -- 原定设计 公钥加密 私钥解密
        try {
            gatewayFlowLog.setFlowId(UidUtil.getTransactionId());
            gatewayFlowLog.setDealStartTime(TimeUtil.format(Instant.now()));
            FlowLogThreadLocal.setFlowLog(gatewayFlowLog);
            proceed =(Mono<Response>) point.proceed();//环绕执行方法进行
            //响应配置信息
            gatewayFlowLog.setDealEndTime(TimeUtil.format(Instant.now()));
            FlowLogThreadLocal.setFlowLog(gatewayFlowLog);
        } catch (UnknownHostException e) {
            log.info("全局流水号生成失败！");
            e.printStackTrace();
        } catch (Throwable throwable) {
            log.info("执行时出现异常");
            throwable.printStackTrace();
        } finally {
            //落地日志信息
            log.info("开始落地流水信息...");
            boolean flag = flowLogService.insertFlow(FlowLogThreadLocal.getFlowLog()); //记录完日志销毁对象
            if(flag) log.info("流水落地成功！");
            else log.info("流水落地失败！");
            FlowLogThreadLocal.remove();
        }
        return proceed;
    }

}
