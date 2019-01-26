package com.hanson.im.common.utils;

import sun.security.provider.MD5;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/26
 * @Description:
 */
public class HashUtil {

    private static HashUtil hashUtil = new HashUtil();

    public static HashUtil getHashUtil() {
        return hashUtil;
    }

    public String md5(List<String> list){
        StringBuilder sb = new StringBuilder();
        list.forEach(str->sb.append(str));
        String str = sb.toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            return  new BigInteger(digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
