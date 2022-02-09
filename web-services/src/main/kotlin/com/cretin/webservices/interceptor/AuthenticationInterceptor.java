package com.cretin.webservices.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
//        if (AppConstants.Companion.isSignRequest()) {
//            if(ApiWhiteListConfig.INSTANCE.checkApi(httpServletRequest.getServletPath())){
//                if (!httpServletRequest.getServletPath().startsWith("/rpc_service")) {
//                    StringBuilder signParams = new StringBuilder();
//                    String time = httpServletRequest.getParameter("time");
//                    String sign = httpServletRequest.getParameter("sign");
//                    if (time == null || sign == null) {
//                        throw new WholeException("身份验证失败");
//                    }
//                    //检验合法性
//                    List<String> paramsList = new ArrayList<>();
//                    for (Map.Entry<String, String[]> stringEntry : httpServletRequest.getParameterMap().entrySet()) {
//                        String key = URLDecoderString(stringEntry.getKey());
//                        if (!key.equals("time") && !key.equals("sign")) {
//                            String value = "";
//                            if (stringEntry.getValue().length > 0) {
//                                value = URLDecoderString(stringEntry.getValue()[0]);
//                            }
//                            key = key.replaceAll("\n", " ");
//                            value = value.replaceAll("\n", " ");
//                            paramsList.add(key + "=" + value + "&");
//                        }
//                    }
//
//                    Collections.sort(paramsList);
//                    StringBuilder params = new StringBuilder();
//                    for (String s : paramsList) {
//                        params.append(s);
//                    }
//                    if (!TextUtils.isEmpty(params)) {
//                        signParams.append(params.substring(0, params.length() - 1));
//                    }
//                    signParams.append(time);
//                    String locaSign = MD5Utils.encode(signParams.toString(), "utf8");
//                    if (!sign.equals(locaSign)) {
//                        throw new WholeException("身份验证失败");
//                    }
//                }
//            }
//        }
////        String uk = httpServletRequest.getHeader("uk");// 从 http 请求头中取出 token
////        String channel = httpServletRequest.getHeader("channel");// 从 http 请求头中取出 token
////        String app = httpServletRequest.getHeader("app");// 从 http 请求头中取出 token
//        // 如果不是映射到方法直接通过
//        if (!(object instanceof HandlerMethod)) {
//            return true;
//        }
//
//        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
//
//        HandlerMethod handlerMethod = (HandlerMethod) object;
//        Method method = handlerMethod.getMethod();
//
//        int userId;
//        TbUser user = null;
//
//        //检查有没有需要用户权限的注解
//        if (method.isAnnotationPresent(UserLoginToken.class)) {
//            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
//            if (userLoginToken.required()) {
//                // 执行认证
//                if (token == null) {
//                    throw new WholeException("用户登录过期");
//                }
//                // 获取 token 中的 user id
//                try {
//                    userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));
//                } catch (JWTDecodeException j) {
//                    throw new WholeException(202, "用户登录过期");
//                }
//                user = userService.getById(userId);
//                if (user == null) {
//                    throw new RuntimeException("用户不存在，请重新登录");
//                }
//                UserAuthHelper.INSTANCE.checkUser(user.getStatus());
//                // 验证 token
//                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
//                try {
//                    jwtVerifier.verify(token);
//                } catch (JWTVerificationException e) {
//                    throw new WholeException(202, "用户登录过期");
//                }
//                return true;
//            }
//        }
//
//        if (!TextUtils.isEmpty(token)) {
//            if (method.isAnnotationPresent(UserAuthCheck.class)) {
//                UserAuthCheck userAuthCheck = method.getAnnotation(UserAuthCheck.class);
//                if (userAuthCheck.required()) {
//                    if (user == null) {
//                        // 获取 token 中的 user id
//                        try {
//                            userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));
//                        } catch (JWTDecodeException j) {
//                            throw new WholeException(202, "用户登录过期");
//                        }
//                        user = userService.getById(userId);
//                        if (user != null) {
//                            UserAuthHelper.INSTANCE.checkUser(user.getStatus());
//                        }
//                    }
//                    return true;
//                }
//            }
//        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}