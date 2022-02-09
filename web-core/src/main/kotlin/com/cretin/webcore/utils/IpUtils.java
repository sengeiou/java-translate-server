/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: IpUtils
 * Author:   cretin
 * Date:     11/24/18 23:43
 * Description: 获取ip的工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cretin.webcore.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * 〈获取ip的工具类〉
 *
 * @author cretin
 * @create 11/24/18
 * @since 1.0.0
 */
public class IpUtils {
    /**
     * @Description: 获取客户端IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) ) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) ) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) ) {
            ip = request.getHeader("X-Real-IP");
        }
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) ) {
            ip = request.getRemoteAddr();
            if ( ip.equals("127.0.0.1") ) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if ( ip != null && ip.length() > 15 ) {
            if ( ip.indexOf(",") > 0 ) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }


    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipCheck(String text) {
        if ( text != null && !text.isEmpty() ) { // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if ( text.matches(regex) ) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }
}