package com.hanson.im.client.logic;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.body.UserInfo;

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

    void buildChannel(UserInfo userInfo);

    void loginBack(boolean result,int code,UserInfo userInfo);

    void disconnect();

    void receiveMessage(Message message);
}
