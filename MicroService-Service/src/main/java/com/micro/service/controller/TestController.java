package com.micro.service.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname TestController
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/15 17:34
 * @Created by yangle
 */
@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @PostMapping("/test1")
    public String test1(@RequestBody JSONObject json){
        log.info("进来了");
        log.info("id={},name={}",json.getString("id"),json.getString("name"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",json.getString("name"));
        return jsonObject.toJSONString();
    }

}
