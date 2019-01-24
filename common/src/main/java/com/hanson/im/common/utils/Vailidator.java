package com.hanson.im.common.utils;

import java.util.regex.Pattern;

/**
 * @author hanson
 * @Date 2019/1/18
 * @Description:
 */
public class Vailidator {

    public static boolean vailidateUserId(String userId){
        if(userId == null) return false;
        return Pattern.matches("^[0-9a-zA-Z]{5,12}$",userId);
    }
    public static boolean vailidatePassword(){
        return true;
    }
}
