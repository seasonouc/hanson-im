package com.hanson.im.server.user;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
public class UserInfo {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String id;
    private String userName;
}
