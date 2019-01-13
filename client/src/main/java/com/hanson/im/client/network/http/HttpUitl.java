package com.hanson.im.client.network.http;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class HttpUitl {

    private static HttpUitl httpUitl = null;

    public static HttpUitl getHttUtil() {
        if (httpUitl == null) {
            synchronized (HttpUitl.class) {
                if (httpUitl == null) {
                    httpUitl = new HttpUitl();
                }
            }
        }
        return httpUitl;
    }

    public String post(String str) {
        return null;
    }

    public String get(String str) {
        return null;
    }

}
