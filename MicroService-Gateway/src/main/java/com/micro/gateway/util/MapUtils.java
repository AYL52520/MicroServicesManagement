package com.micro.gateway.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Classname MapUtils
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/14 14:56
 * @Created by yangle
 */
@Log4j2
public class MapUtils {

    /** 频率限制保存
     * key:可是短信模板code+手机号或ip
     * value:ConcurrentHashMap<String, Integer>
     *     key:秒级别的key,每秒只会产生一个
     *     value:key对应的秒，这一秒内发送的次数
     */
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> sendMap = new ConcurrentHashMap<>();
    /** 频率限制定时清除 */
    private static ConcurrentHashMap<String, Long> releaseMap = new ConcurrentHashMap<>();

    /** 验证码保存 key:可以是短信模板code+手机号，value:短信验证码 */
    public static ConcurrentHashMap<String, String> captchaMap = new ConcurrentHashMap<>();
    /** 短信验证码定时清除 */
    private static ConcurrentHashMap<String, Long> captchaReleaseMap = new ConcurrentHashMap<>();

    static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);

    static {
        // 每秒一次循环清除频率限制数据，是对失效的数据清除，以防积累大量的垃圾数据
        executorService.scheduleWithFixedDelay(() -> {
            synchronized (releaseMap){
                long now = System.currentTimeMillis();
                for(String key : releaseMap.keySet()) {
                    Long aLong = releaseMap.get(key);
                    if(aLong < now) {
                        releaseMap.remove(key);
                        sendMap.remove(key);
                    }
                }
                System.out.println("releaseMap:" + releaseMap);
                System.out.println("sendMap:" + sendMap);
            }
        }, 1, 1, TimeUnit.SECONDS);

        // 每秒一次循环清除过期的验证码数据
        executorService.scheduleWithFixedDelay(() -> {
            synchronized (captchaReleaseMap){
                long now = System.currentTimeMillis();
                for(String key : captchaReleaseMap.keySet()) {
                    Long aLong = captchaReleaseMap.get(key);
                    if(aLong < now) {
                        captchaReleaseMap.remove(key);
                        captchaMap.remove(key);
                    }
                }
                System.out.println("captchaReleaseMap:" + captchaReleaseMap);
                System.out.println("captchaMap:" + captchaMap);
            }

        },1, 1, TimeUnit.SECONDS);
    }


    /**
     * 用于控制用户在某个时间窗口内的访问次数
     * 比如：发送短信业务，300秒内可以发送5次，60秒内发送1次
     * @param key 限制用户的标识，比如手机号、ip地址
     * @param limitTime 限制的时间间隔，比如：300s
     * @param limitTimes 限制的时间间隔内的次数，比如：5次
     * @return false 表示未通过，true 表示通过
     */
    public static boolean applyRollingTimeWindow(String key, Integer limitTime, Integer limitTimes) {

        synchronized (releaseMap) {
            Date nowDate = new Date();
            // yyyyMMddHHmmss
            String now = DatePattern.PURE_DATETIME_FORMAT.format(nowDate);
            String before = DatePattern.PURE_DATETIME_FORMAT.format(DateUtil.offsetSecond(nowDate, -limitTime));
            long beforeLong = Long.parseLong(before);

            ConcurrentHashMap<String, Integer> keyMap = sendMap.getOrDefault(key, new ConcurrentHashMap<>());
            // 当前key 第一次进入
            if(keyMap.size() < 1) {
                keyMap.put(now, 1);
                sendMap.put(key, keyMap);
                releaseMap.put(key, DateUtil.offsetSecond(nowDate, limitTime).getTime());
                return true;
            }

            // 当前key之前进来过，所以会有之前的秒数对应的值存进去，将时间限制范围以外的删掉
            for(String second : keyMap.keySet()) {
                long secondLong = Long.parseLong(second);
                if(secondLong < beforeLong) {
                    keyMap.remove(second);
                }
            }

            // 累加一下value，看有没有达到限制的次数limitTimes
            Enumeration<Integer> elements = keyMap.elements();
            Integer sum = elements.nextElement();
            while (elements.hasMoreElements()) {
                sum+= elements.nextElement();
            }
            // 已经达到限制次数，就不能通过了
            if(sum >= limitTimes) {
                return false;
            }

            // 还没有达到限制的次数，就把当前秒累加加进去
            keyMap.put(now, keyMap.getOrDefault(now, 0) + 1);
            releaseMap.put(key, DateUtil.offsetSecond(nowDate, limitTime).getTime());
            return true;
        }
    }

    /**
     * 设置key-value，并设置有效期
     * @param key
     * @param value
     * @param survivalTime
     */
    public static void put(String key, String value, Integer survivalTime) {
        synchronized (captchaReleaseMap){
            captchaMap.put(key, value);
            captchaReleaseMap.put(key, DateUtil.offsetSecond(new Date(), survivalTime).getTime());
        }
    }

    /**
     * 获取key对应的value
     * @param key
     * @return
     */
    public static String get(String key) {
        return captchaMap.get(key);
    }

    /**
     * 移除key
     * @param key
     */
    public static void remove(String key) {
        synchronized (captchaReleaseMap){
            captchaMap.remove(key);
            captchaReleaseMap.remove(key);
        }
    }
    public static void main(String[] args) throws InterruptedException {

        System.out.println(1<1);
    }
}
