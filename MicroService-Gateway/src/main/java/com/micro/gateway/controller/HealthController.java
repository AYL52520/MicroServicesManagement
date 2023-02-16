package com.micro.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname HealthController
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/8 14:35
 * @Created by yangle
 */
@RestController
@RequestMapping("actuator")
public class HealthController {

    @RequestMapping("/health")
    public String health(){
        return "up";
    }

}
