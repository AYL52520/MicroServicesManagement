package com.micro.service.filter;

import com.micro.service.constant.SystemConstant;
import com.micro.service.data.TokenThreadLocal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yang.micro.exception.SDKException;
import yang.micro.uitl.GMEncryptionUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname DecryptAndSignCheckGatewayFilterFactory
 * 来自页面的解密验签
 * @Version 1.0.0
 * @Date 2023/3/8 17:30
 * @Created by yangle
 */
@Log4j2
@Component
@RefreshScope
public class DecryptAndSignCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<DecryptAndSignCheckGatewayFilterFactory.Config> {

    private static final String KEY = "withParams";
    private static final String ORDER = "order";
    @Value("${routeconfiguration.privateKey}")
    private String privateKey;

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY,ORDER);
    }

    public DecryptAndSignCheckGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        log.info("开始执行请求报文解密验签！");
        return new InnerFilter(config);
    }

    private class InnerFilter implements GatewayFilter, Ordered {

        private Config config;

        InnerFilter(Config config) {
            this.config = config;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                ServerHttpRequest request = exchange.getRequest();
                URI uri = request.getURI();
                String token = request.getHeaders().get(SystemConstant.YANG_TOKEN).get(0).split("_")[1];
                TokenThreadLocal.setToken(token);
                log.info("请求的URI={}",uri);
                return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    //获取到原始报文 - 开始进行加解密
                    String bodyString = new String(bytes);
                    log.info("原始密文请求参数:{}",bodyString);
                    /**
                     * 原始报文解密验签
                     */
                    String decryptData = null;
                    try {
                        decryptData = GMEncryptionUtils.decryptFromH5Date(privateKey,token,bodyString);
                    } catch (SDKException e) {
                        log.info("解密验签时发成异常_"+e.getMessage(),e);
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    exchange.getAttributes().put("POST_BODY",decryptData);
                    byte[] newBytes = decryptData.getBytes(StandardCharsets.UTF_8);
                    DataBufferUtils.release(dataBuffer);
                    Flux<DataBuffer> cachedFlux = Flux.defer(()->{
                        DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(newBytes);
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
            return config.getOrder();
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
