package com.micro.service.service;

import com.micro.service.pojo.RouteConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname IRouteConfigService
 * @Description 路由类型服务类
 * @Version 1.0.0
 * @Date 2023/1/31 20:55
 * @Created by yangle
 */
public interface IRouteConfigService {

    RouteConfig selectByMidAndJwid(String mid , String jwid);

    public static void main(String[] args) throws ParseException {
//        将指定格式的时间转换为时间戳：
        String time = "2023-02-10 12:23:30";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//首先定义待转换的时间格式
        Date date = sdf.parse(time);//将带转换的时间字符串转换为date类型，然后使用getTime即可获取对应的时间戳

//如果是Date类型的时间，直接使用date.getTime就可以获得其对应的毫秒级时间戳：
        Long time1 = date.getTime();//Date类中就保存有毫秒时间戳变量

        System.out.println(time1);
    }
}
