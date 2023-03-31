package com.micro.service.filter;

import com.micro.service.bean.BodyHandlerFunction;
import com.micro.service.bean.BodyHandlerServerHttpResponseDecorator;
import com.micro.service.data.TokenThreadLocal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yang.micro.exception.SDKException;
import yang.micro.uitl.GMEncryptionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname SignAndEncryptGatewayFilterFactory
 * 加密加签服务
 * @Version 1.0.0
 * @Date 2023/3/8 15:30
 * @Created by yangle
 */
@Log4j2
@Component
public class SignAndEncryptGatewayFilterFactory extends AbstractGatewayFilterFactory<SignAndEncryptGatewayFilterFactory.Config> {

    private static final String KEY = "withParams";
    private static final String ORDER = "order";
    @Value("${routeconfiguration.privateKey}")
    private String privateKey;

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY, ORDER);
    }

    public SignAndEncryptGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        log.info("开始执行响应报文加密！");
        return new InnerFilter(config);
    }

    /**
     * 创建一个内部类，来实现2个接口，指定顺序
     * 这里通过Ordered指定优先级
     */
    private class InnerFilter implements GatewayFilter, Ordered {

        private Config config;

        InnerFilter(Config config) {
            this.config = config;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            String token = TokenThreadLocal.getToken();
            if (!config.isWithParams()) {
                return chain.filter(exchange);
            }

            log.info("Test1GlobalFilter进入此方法");
            //构建响应拦截处理器
            BodyHandlerFunction bodyHandler = (resp, body) -> Flux.from(body)
                    .map(dataBuffer -> {
                        //响应信息转换为字符串
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        String reqBody = new String(content, StandardCharsets.UTF_8);
                        log.info("响应的原始报文" + reqBody);
                        return reqBody;
                    }).flatMap(orgBody -> {
                        String encryptStr = "";
                        try {
                            encryptStr = GMEncryptionUtils.encryptToH5Date(privateKey, token, orgBody);
                        } catch (SDKException | IOException e) {
                            e.printStackTrace();
                        } finally {
                            TokenThreadLocal.remove();
                        }
                        HttpHeaders headers = resp.getHeaders();
                        headers.setContentLength(encryptStr.length());
                        return resp.writeWith(Flux.just(encryptStr).map(bx -> resp.bufferFactory()
                                .wrap(bx.getBytes())));
                    }).then();
            //构建响应包装类
            BodyHandlerServerHttpResponseDecorator responseDecorator = new BodyHandlerServerHttpResponseDecorator(
                    bodyHandler, exchange.getResponse());
            return chain.filter(exchange.mutate().response(responseDecorator).build());
        }

        @Override
        public int getOrder() {
            return config.getOrder();
        }
    }

    public static class Config {

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
