package com.micro.service.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname HttpRequestUtil
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/2 21:05
 * @Created by yangle
 */
@Component
public class HttpRequestUtil {

    @Autowired
    private RestTemplate restTemplate;

    public Object doPost(String url, HttpHeaders httpHeaders, JSONObject params) {
        HttpEntity<Object> request = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<Object> result = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
        if (null != result && null != result.getBody()) {
            return result.getBody();
        }
        return null;
    }

    public Object doPostJson(String url, HttpHeaders httpHeaders, JSONObject params) {
        HttpEntity<Object> request = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<JSONObject> result = restTemplate.exchange(url, HttpMethod.POST, request, JSONObject.class);
        if (null != result && null != result.getBody()) {
            return result.getBody();
        }
        return null;
    }

    public Object doGet(String url){
        return restTemplate.getForObject(url,String.class);
    }

}
