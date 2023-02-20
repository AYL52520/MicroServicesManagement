package com.micro.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于配置实现路由
 */
@Data
@Component(RouteConfiguration.NAME)
public class RouteConfiguration extends ConcurrentHashMap<String, RouteConfiguration.Route> implements Listable,Loadable,Saveable{
    public static final String NAME = "routeConfiguration";

    @Override
    public List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();
        this.forEach((k,v) ->{
            Map<String, Object> map = new HashMap<>();
            map.put("id",k);
            map.put("serviceId",v.getServiceId());
            map.put("uri",v.getUri());
            map.put("predicates",v.getPredicates());
            map.put("filters",v.getFilters());
            list.add(map);
        });
        return list;
    }

    @Override
    public void load(JSONObject json) {
        Map<String, Route> newMap = new ConcurrentHashMap<>();
        json.forEach((k,v) -> {
            Route route = new Route();
            JSONObject subJson = (JSONObject) v;
            route.setId(subJson.getString("id"));
            route.setServiceId(subJson.getString("serviceId"));
            route.setUri(subJson.getString("uri"));
            route.setPredicates(subJson.getString("predicates"));
            route.setFilters(subJson.getString("filters"));
            newMap.put(k, route);
        });

    }

    @Override
    public JSONObject save() {
        return (JSONObject) JSON.toJSON(this);
    }


    @Data
    public static class Route {
        private String id;
        private String serviceId;
        private String uri;
        private String predicates;
        private String filters;
    }


}
