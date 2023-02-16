package com.micro.gateway.util;

import lombok.extern.log4j.Log4j2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * @Classname UidUtil
 * @Version 1.0.0
 * @Date 2023/2/7 15:52
 * @Created by yangle
 * 生成30位唯一数: yyyyMMddHHmmssSSS+服务器IP后5位+3位递增序号+5位随机数
 * 如果将系统部署到集群上面，情况有会有不同了，不同的服务器集群生成的这个数字，是有重合的概率的，
 * 因此，一般情况是，将集群中的每个机器进行IP编码，然后将机器编码放在这个标识中以示区分
 *
 */
@Log4j2
public class UidUtil {
    private static String no = "0";
    private static String dateValue = "";//默认精确到毫秒的比对时间
    private static Random rand = new Random();

    public static synchronized String next() throws UnknownHostException {
        String ipStr = InetAddress.getLocalHost().getHostAddress();// 获取本机IP
        ipStr = ipStr.replace(".", "").substring(ipStr.length()-5);
        if(ipStr.length() > 5){
            ipStr = ipStr.substring(0, 5);
        }
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String num = new StringBuilder().append(dateStr)
                .append(ipStr).toString();
        if (!(String.valueOf(dateStr)).equals(dateValue)) {//不是同一个时段，从0开始递增序号
            no = "0";
            dateValue = dateStr;
        }
        num += getNo(no, num);
        return num;
    }
    /**
     * 返回当前序号+1
     */
    public static String getNo(String noCount, String num) {
        long i = Long.parseLong(noCount);
        i += 1;
        noCount = "" + i;
        for (int j = noCount.length(); j < 25 - num.length(); j++) {
            noCount = "0" + noCount;
        }
        no = noCount;
        return noCount;
    }

    /**
     * 利用Set中不允许有重复的元素的特性，来判断集合元素中是否有重复元素
     *
     * @param list:被判断的集合
     * @return false:有重复元素或list为null; true:没有重复元素
     */
    private static boolean hasSame(List<? extends Object> list) {
        if (null == list)
            return false;
        return list.size() == new HashSet<Object>(list).size();
    }

    /**
     * next()+5位随机数
     * @return
     * @throws UnknownHostException
     */
    public static String getTransactionId() throws UnknownHostException{
        return new StringBuilder().append(next())
                .append(String.valueOf(rand.nextInt(99999 - 10000 + 1) + 10000)).toString();
    }

    /*public static void main(String[] args) {
        final List<String> guidList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {// 开启10个线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {// 每个线程循环1w条数据
                        try {
                            String guid = getTransactionId();
                            System.out.println("guid="+guid+",length="+guid.length());
                            guidList.add(guid);
                        } catch (UnknownHostException e) {
                            log.error("未知主机IP错误-->【{}】", e);
                        }
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(30000);// sleep 30秒
            log.info("集合个数-->【{}】", guidList.size());
            log.info("没有重复元素-->【{}】", hasSame(guidList));
        } catch (InterruptedException e) {
            log.error("sleep error-->【{}】", e);
        }
    }*/
}
