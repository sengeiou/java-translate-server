/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: RestTemplateUtils
 * Author:   cretin
 * Date:     8/6/19 21:09
 * Description: 网络的工具了
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cretin.translatetools.utils;

import com.cretin.translatetools.utils.GsonGet;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 〈网络的工具〉
 *
 * @author cretin
 * @create 8/6/19
 * @since 1.0.0
 */
public class RestTemplateUtils {

    /**
     * RestTemplate POST 请求
     *
     * @param restTemplate
     * @param url
     * @param map
     * @return
     */
    public static String doPost(RestTemplate restTemplate, String url, Map<String, Object> map) throws UnsupportedEncodingException {
//        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
//        if ( map != null ) {
//            for ( Map.Entry<String, Object> entry : map.entrySet() ) {
//                postParameters.add(entry.getKey(), entry.getValue());
//            }
//        }
//        //拿到header信息
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//        requestHeaders.setAccept(Arrays.asList(MediaType.ALL));
//        requestHeaders.setConnection("Keep-Alive");
//        requestHeaders.add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//        HttpEntity requestEntity = new HttpEntity(postParameters, requestHeaders);
//        return restTemplate.postForObject(url, requestEntity, String.class);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(GsonGet.INSTANCE.getGson().toJson(map), headers);
        return new String(restTemplate.postForEntity(url, formEntity, String.class).getBody().getBytes("ISO-8859-1"),"utf-8");
    }

    public static String doPost(RestTemplate restTemplate, String url, Object object) throws UnsupportedEncodingException {
//        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
//        if ( map != null ) {
//            for ( Map.Entry<String, Object> entry : map.entrySet() ) {
//                postParameters.add(entry.getKey(), entry.getValue());
//            }
//        }
//        //拿到header信息
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//        requestHeaders.setAccept(Arrays.asList(MediaType.ALL));
//        requestHeaders.setConnection("Keep-Alive");
//        requestHeaders.add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//        HttpEntity requestEntity = new HttpEntity(postParameters, requestHeaders);
//        return restTemplate.postForObject(url, requestEntity, String.class);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(object == null ? "{}" : GsonGet.INSTANCE.getGson().toJson(object), headers);
        return new String(restTemplate.postForEntity(url, formEntity, String.class).getBody().getBytes("ISO-8859-1"),"utf-8");
    }

    /**
     * 下载文件
     *
     * @param restTemplate
     * @param url
     * @return
     */
    public static byte[] downloadFile(RestTemplate restTemplate, String url) {
        HttpHeaders headers = new HttpHeaders();
        List list = new ArrayList<>();
        list.add(MediaType.IMAGE_GIF);
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        headers.setAccept(list);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<byte[]>(headers),
                byte[].class);

        return response.getBody();
    }

    public static InputStream downloadFileByStream(RestTemplate restTemplate, String url) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        List list = new ArrayList<>();
        list.add(MediaType.IMAGE_GIF);
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        headers.setAccept(list);

        ResponseEntity<Resource> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<byte[]>(headers),
                Resource.class);

        return response.getBody().getInputStream();
    }
}