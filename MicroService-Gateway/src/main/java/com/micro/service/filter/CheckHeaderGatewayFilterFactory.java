package com.micro.service.filter;

import com.micro.service.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yang.micro.exception.SDKException;
import yang.micro.exception.SDKExceptionEnums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Classname CheckHeaderGatewayFilterFactory
 * 校验头中的Token信息
 * @Version 1.0.0
 * @Date 2023/3/9 9:52
 * @Created by yangle
 */
@Slf4j
@Component
public class CheckHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<CheckHeaderGatewayFilterFactory.Config> {

    private static final String KEY = "withParams";
    private static final String ORDER = "order";

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY,ORDER);
    }

    public CheckHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        log.info("开始执行校验报文请求头信息！");
        return new InnerFilter(config);
    }

    private class InnerFilter implements GatewayFilter, Ordered {

        private Config config;

        InnerFilter(Config config) {
            this.config = config;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            //校验头中是否存在token,并讲token存储至上下文中
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();
            if(!headers.containsKey(SystemConstant.YANG_TOKEN) || Objects.isNull(headers.get(SystemConstant.YANG_TOKEN))
                    || !Objects.requireNonNull(headers.get(SystemConstant.YANG_TOKEN)).get(0).contains("YANG_")){
                try {
                    throw new SDKException(SDKExceptionEnums.OAUTH_TOKEN_ERROR,"请求头中未携带身份信息或身份信息为空");
                } catch (SDKException e) {
                    log.info("请求头中未携带身份信息/身份信息为空/未按照要求提交!");
                    e.printStackTrace();
                }
            }
            //获取到token信息 校验格式
            String token = Objects.requireNonNull(headers.get(SystemConstant.YANG_TOKEN)).get(0).split("_")[1];
            /**
             * 实际颁发token信息时，会存储至redis中，并且绑定商户信息，存储时考虑过期时间等等
             * ① redis实现token校验
             */
            // TODO 目前redis校验还未实现
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                Flux<DataBuffer> cachedFlux = Flux.defer(()->{
                    DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(bytes);
                    return Mono.just(wrap);
                });
                ServerHttpRequest httpRequestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()){
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                return chain.filter(exchange.mutate().request(httpRequestDecorator).build());
            });
        }

        @Override
        public int getOrder() {
            return config.order;
        }
    }

    public static class Config{

        private boolean withParams;
        private int order;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public boolean isWithParams() {
            return withParams;
        }

        public void setWithParams(boolean withParams) {
            this.withParams = withParams;
        }
    }

}
