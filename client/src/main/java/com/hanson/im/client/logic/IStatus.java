package com.hanson.im.client.logic;

import com.hanson.im.common.protocol.Message;

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

    void loginBack(boolean result,int code);

    void disconnect();

    void receiveMessage(Message message);
}
