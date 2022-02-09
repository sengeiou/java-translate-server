package com.cretin.webcore.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DEStool {

    private static DEStool deStool;

    private String sKey;

    private DEStool() {
        //默认构造函数提供默认密钥
        sKey = "cretin**273846";
    }

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encrptText(String text) {
        if (deStool == null) {
            deStool = new DEStool();
        }
        try {
            return deStool.encrypt(text);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 解密
     *
     * @param text
     * @return
     */
    public static String decryptText(String text) {
        if (deStool == null) {
            deStool = new DEStool();
        }
        try {
            return deStool.decrypt(text);
        } catch (Exception e) {
            return "";
        }
    }

    private Cipher makeCipher() throws Exception {
        return Cipher.getInstance("DES");
    }

    private SecretKey makeKeyFactory() throws Exception {
        SecretKeyFactory des = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = des.generateSecret(new DESKeySpec(sKey.getBytes()));
        return secretKey;
    }

    public String encrypt(String text) throws Exception {
        Cipher cipher = makeCipher();
        SecretKey secretKey = makeKeyFactory();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return new String(Base64.encodeBase64(cipher.doFinal(text.getBytes())));
    }

    public String decrypt(String text) throws Exception {
        Cipher cipher = makeCipher();
        SecretKey secretKey = makeKeyFactory();
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.decodeBase64(text.getBytes())));
    }

    public static void main(String[] args) {
        System.out.println(decryptText("6o4pmAeqOKgoVyhFuxgF0N0ySA8QIHQ96F8KBsBVdxcMalL5wN5fnuuoS9dW/FXiq9EjQsoVrlqhZ2umWsDMMtE4SRcAagpv"));
    }
}