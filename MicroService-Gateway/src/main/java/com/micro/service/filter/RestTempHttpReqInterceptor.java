package com.micro.service.filter;

import com.alibaba.fastjson.JSON;
import com.micro.service.pojo.GatewayFlowLog;
import com.micro.service.data.FlowLogThreadLocal;
import com.micro.service.util.TimeUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * @Classname RequestFlowLogInterceptor
 * 记录请求/响应体日志信息 这里可以添加上下文信息 并释放
 * 作为
 * @Version 1.0.0
 * @Date 2023/2/5 19:44
 * @Created by yangle
 */
@Log4j2
public class RestTempHttpReqInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        GatewayFlowLog flowLog = FlowLogThreadLocal.getFlowLog();
        //记录请求信息 - 记录请求时间
        HttpHeaders headers = request.getHeaders();
        log.info("RestTemplate记录请求信息: URL={},请求信息={},请求头={}",
                request.getURI(),new String(body, StandardCharsets.UTF_8), JSON.toJSONString(headers));
        flowLog.setRouteReqTime(TimeUtil.format(Instant.now()));
        ClientHttpResponse response = execution.execute(request, body);
        flowLog.setRouteReqTime(TimeUtil.format(Instant.now()));
        //记录响应信息 - 并记录请求时间
        log.info("RestTemplate记录响应信息: 请求信息={}",IOUtils.toString(response.getBody(),"UTF-8"));

        return response;
    }
}
