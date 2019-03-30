package com.coder520.common.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/17.
 */
public class SecurityUtils {

    public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String result = base64Encoder.encode(md5.digest(password.getBytes("utf-8")));

        return result;
    }

    public  static boolean  checkPassword(String inputPwd,String dbPwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
         String result = encryptPassword(inputPwd);
         if(result.equals(dbPwd)){
             return true;
         }else {
             return false;
         }
    }
}
