package com.cretin.webcore.exception;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;


/**
 * controller 增强器
 *
 * @author sam
 * @since 2017/7/17
 */
@RestControllerAdvice
public class GlobalControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler( value = Exception.class )
    public Map errorHandler(Exception ex) {
        if ( ex instanceof ClientAbortException) {
            return null;
        }
        logger.error("程序抛出系统异常啦！" + ex.getMessage());
        Map map = new HashMap();
        map.put("code", 0);
        if ( ex instanceof MethodArgumentTypeMismatchException) {
            map.put("msg", "参数" + ((MethodArgumentTypeMismatchException) ex).getName() + "格式异常");
        } else if ( ex instanceof MissingServletRequestParameterException) {
            map.put("msg", "参数" + ((MissingServletRequestParameterException) ex).getParameterName() + "为必传项");
        } else if ( ex instanceof MissingServletRequestPartException) {
            map.put("msg", "参数" + ((MissingServletRequestPartException) ex).getRequestPartName() + "为必传项");
        } else if ( ex instanceof HttpRequestMethodNotSupportedException) {
            map.put("msg", "此方法不支持" + ((HttpRequestMethodNotSupportedException) ex).getMethod() + "请求");
        } else if ( ex instanceof MultipartException) {
            map.put("msg", "上传的文件不能为空");
        } else if ( ex instanceof RedisConnectionFailureException ) {
            map.put("msg", "redis未启动");
        } else {
            map.put("msg", "请求超时,请稍后再试");
        }
        return map;
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler( value = WholeException.class )
    public Map myErrorHandler(WholeException ex) {
        Map map = new HashMap();
        map.put("code", ex.getCode());
        map.put("msg", ex.getMsg());
        return map;
    }
}