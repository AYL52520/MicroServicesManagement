package com.micro.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.micro.gateway.util.ProjectUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于配置实现路由
 */
@Data
@Log4j2
@RefreshScope
@Component(RouteConfiguration.NAME)
public class RouteConfiguration extends ConcurrentHashMap<String, RouteDefinition> implements Listable, Loadable, Saveable {
    public static final String NAME = "routeConfiguration";
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<Object> toList() {
        List<Object> list = new ArrayList<>();
        this.forEach((k, v) -> {
            list.add(this.get(k));
        });
        return list;
    }

    //将路由数据写入spring容器对象中
    @Override
    public void load(Map<String, RouteDefinition> loadMap) {
        ProjectUtil.reloadMap(this, loadMap);
        //将路由对象进行更新
        //加载至缓存中
        this.toList().stream().forEach(routeDefinition -> {
            routeDefinitionWriter.save(Mono.just((RouteDefinition) routeDefinition)).subscribe();
        });
        //通过应用内消息的方式发布
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));
    }

    @Override
    public JSONObject save() {
        return (JSONObject) JSON.toJSON(this);
    }


    public boolean compareMap(Map<String,RouteDefinition> cMap) {
        AtomicBoolean equals = new AtomicBoolean(true);
        //元素数量不同不相等
        if (cMap.size() != this.size())
            equals.set(false);
        this.forEach((k,v)->{
            if(!v.equals(cMap.get(k))){
                equals.set(false);
            }
        });
        return equals.get();
    }
}
