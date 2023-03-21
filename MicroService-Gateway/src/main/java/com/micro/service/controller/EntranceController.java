package com.micro.service.controller;

import com.alibaba.fastjson.JSON;
import com.micro.service.data.FlowLogThreadLocal;
import com.micro.service.data.Request;
import com.micro.service.data.Response;
import com.micro.service.pojo.GatewayFlowLog;
import com.micro.service.pojo.RouteConfig;
import com.micro.service.service.IRouteConfigService;
import com.micro.service.util.HttpRequestUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


/**
 * @Classname EntranceController
 * @Description 统一入口，进来后校验 进行数据路由
 * @Version 1.0.0
 * @Date 2023/1/31 10:58
 * @Created by yangle
 */
@Log4j2
@RestController()
@RequestMapping
public class EntranceController {

    @Autowired
    private HttpRequestUtil httpRequestUtil;
    @Autowired
    private IRouteConfigService routeConfigService;
    /**
     * 统一入口 http://ip:poet/entrance
     * {"HEAD":{uri:xxx,METHOD:POST/GET,jwid:xxx},"BODY":{加密的JSON}}
     * 数据库存储地址
     */
    @PostMapping("/entrance")
    public Mono<Response> entrance(ServerWebExchange exchange) {
        GatewayFlowLog flowLog = FlowLogThreadLocal.getFlowLog();

        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            String bodyString = new String(bytes, StandardCharsets.UTF_8);
            log.info("原始的请求报文：{}",bodyString);
            Request request = JSON.parseObject(bodyString, Request.class);

            String jwid = request.getHead().getJwId();
            String method = request.getHead().getMethod();
            String mid = request.getHead().getMid();
            String uri = request.getHead().getUri(); //URI要求String最前端不带/,eg "bass/test"
            flowLog.setAppid(jwid);
            flowLog.setOpenId("openID");
            flowLog.setRouteUrl(uri);
            FlowLogThreadLocal.setFlowLog(flowLog);
            // xxx/01?xxx=x1
            //  01   url(http://ip:port/ + uri)
            //数据库中以一查询，获取到地址 http://ip:port/ + uri
            RouteConfig routeConfig = routeConfigService.selectByMidAndJwid(mid, jwid);
            String URL = routeConfig.getUrl() + uri; //路由的URL
            //路由 请求分行请求
            String  o = (String) httpRequestUtil.doGet(URL);
            log.info("响应结果为{}",o);
            //落地日志信息
            return Mono.just(Response.ok());
        });
    }

    @GetMapping("/demo")
    public String demo(){
        return "success";
    }

}
