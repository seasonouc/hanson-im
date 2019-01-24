package com.hanson.im.client.logic;

import com.hanson.im.common.protocol.Message;

/**
 * @author hanson
 * @Date 2019/1/22
 * @Description:
 */
public interface EventListener {
    void loginCall(boolean result);

    void receivCall(Message message);

    void buildChannelCall(String sessionId);
}
