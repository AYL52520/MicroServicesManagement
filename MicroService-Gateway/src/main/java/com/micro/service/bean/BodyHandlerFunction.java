package com.micro.service.bean;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * @Classname BodyHandlerFunction
 * 重置体响应类
 * @Version 1.0.0
 * @Date 2023/3/16 15:53
 * @Created by yangle
 */
public interface BodyHandlerFunction extends
        BiFunction<ServerHttpResponse, Publisher<? extends DataBuffer>, Mono<Void>> {
}

