package com.hanson.im.client.chat;

/**
 * @author hanson
 * @Date 2019/1/16
 * @Description:
 */
public interface IStatus {
    void setLogin(boolean login);

    boolean getLogin();

    String getMyId();

    String getMyName();

    void buildChannel(String userId);
}
