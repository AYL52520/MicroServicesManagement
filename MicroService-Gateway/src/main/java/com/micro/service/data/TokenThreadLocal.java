package com.micro.service.data;

import org.springframework.stereotype.Component;

/**
 * @Classname FlowLogThreadLocal
 * 创建链接的上下文对象 在上下文中使用
 * @Version 1.0.0
 * @Date 2023/2/7 14:39
 * @Created by yangle
 */
@Component
public class TokenThreadLocal {

    private static ThreadLocal<String> tokenThreadLocal =
            new ThreadLocal<>();

    public static String getToken() {
        return tokenThreadLocal.get();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    //清除对象引用
    public static void remove(){
        tokenThreadLocal.remove();
    }
}
