/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ParamUtils
 * Author:   cretin
 * Date:     4/17/20 11:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cretin.webdb.utils;

import com.cretin.webcore.utils.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author cretin
 * @create 4/17/20
 * @since 1.0.0
 */
public class ParamUtils {

    /**
     * 获取连接中指定参数名的参数
     * @param url
     * @param name
     * @return
     */
    public static String getParamsByName(String url, String name) {
        Map<String, String> paramsMap = getParamsMap(url);
        if(paramsMap.containsKey(name)){
            return paramsMap.get(name);
        }
        return null;
    }

    /**
     * 获取链接中所有的参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getParamsMap(String url) {
        Map<String, String> paramsMap = new HashMap<>();
        if ( !StringUtils.isEmpty(url) )
            try {
                URI uri = new URI(url);
                String query = uri.getQuery();
                String[] split = query.split("&");
                for ( String s : split ) {
                    String[] params = s.split("=");
                    if ( params.length == 2 ) {
                        paramsMap.put(params[0], params[1]);
                    }
                }
            } catch ( Exception e ) {

            }
        return paramsMap;
    }
}