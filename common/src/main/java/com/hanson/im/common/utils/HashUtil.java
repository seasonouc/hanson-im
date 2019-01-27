package com.hanson.im.common.utils;

import com.hanson.im.common.protocol.body.UserInfo;
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

    public String md5(List<UserInfo> list){
        StringBuilder sb = new StringBuilder();
        list.forEach(user->sb.append(user.getId()));
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
