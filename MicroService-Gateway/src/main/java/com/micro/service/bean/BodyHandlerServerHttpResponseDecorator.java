package com.micro.service.bean;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Classname BodyHandlerServerHttpResponseDecorator
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/16 15:52
 * @Created by yangle
 */
public class BodyHandlerServerHttpResponseDecorator
        extends ServerHttpResponseDecorator {

    /**
     * body 处理拦截器
     */
    private BodyHandlerFunction bodyHandler = initDefaultBodyHandler();

    /**
     * 构造函数
     *
     * @param bodyHandler
     * @param delegate
     */
    public BodyHandlerServerHttpResponseDecorator(
            BodyHandlerFunction bodyHandler, ServerHttpResponse delegate) {
        super(delegate);
        if (bodyHandler != null) {
            this.bodyHandler = bodyHandler;
        }
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        //body 拦截处理器处理响应
        return bodyHandler.apply(getDelegate(), body);
    }

    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return this.writeWith(Flux.from(body).flatMapSequential((p) -> {
            return p;
        }));
    }

    /**
     * 默认body拦截处理器
     *
     * @return
     */
    private static BodyHandlerFunction initDefaultBodyHandler() {
        return ReactiveHttpOutputMessage::writeWith;
    }


}
