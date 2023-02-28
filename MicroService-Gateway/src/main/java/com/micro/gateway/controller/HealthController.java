package com.micro.gateway.controller;

import com.micro.gateway.config.MicroConfiguration;
import com.micro.gateway.config.RouteConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@Log4j2
public class HealthController {


    @RequestMapping("/health")
    public String health(){
        return "up";
    }

}
