package com.cretin.webcore.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ctetin on 2018/11/21.
 */
public class StringUtils {
    //校验手机是否合规 2020年最全的国内手机号格式
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

    private static final String FORMAT_RANDOM = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    /**
     * 校验手机号
     *
     * @param phone 手机号
     * @return boolean true:是  false:否
     */
    public static boolean isMobile(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    /**
     * 是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isDeepEmpty(String text) {
        if (text == null)
            return true;
        return isEmpty(text.trim());
    }

    //加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    //判断Email合法性
    public static boolean isEmail(String email) {
        if (email == null)
            return false;
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(rule);
        matcher = pattern.matcher(email);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    /**
     * 格式化数字
     *
     * @param num
     * @return
     */
    public static String formatNum(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return num + "";
    }

    /**
     * 是否所有的都是空字符串
     *
     * @param args
     * @return
     */
    public static boolean isAllEmpty(String... args) {
        for (String arg : args) {
            if (StringUtils.isEmpty(arg)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转码成Base64
     *
     * @param content
     * @return
     */
    public static String encodeToBase64(String content) {
        return new BASE64Encoder().encode(content.getBytes());
    }

    /**
     * Base64位解码
     *
     * @param content
     * @return
     */
    public static String decodeToBase64(String content) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化手机号
     *
     * @param telephone
     * @return
     */
    public static String formatPhone(String telephone) {
        if (telephone == null || telephone.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < telephone.toCharArray().length; i++) {
            if (i < 3 || i > telephone.length() - 5) {
                stringBuilder.append(telephone.charAt(i));
            } else
                stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取最大值max以内的不重复size个数字
     *
     * @param max
     * @param size
     * @return
     */
    public static List<Integer> getRandomIntegerList(int max, int size) {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        int index = 0;
        while (true) {
            int randomIndex = random.nextInt(max) + 1;
            if (!list.contains(randomIndex)) {
                list.add(randomIndex);
                index++;
            }
            if (index >= size) {
                break;
            }
        }
        return list;
    }

    /**
     * 编码id
     *
     * @return
     */
    public static String encodeId(int id) {
        return getRandomStr(10) + id + getRandomStr(10);
    }

    private static String getRandomStr(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < num) {
            stringBuilder.append(FORMAT_RANDOM.charAt(random.nextInt(FORMAT_RANDOM.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * 解码id
     *
     * @return
     */
    public static int decodeId(String str) {
        try {
            return Integer.parseInt(str.substring(10, str.length() - 10));
        } catch (Exception e) {
            return -1;
        }
    }
}
