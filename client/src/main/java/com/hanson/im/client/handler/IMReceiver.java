package com.hanson.im.client.handler;

import com.hanson.im.common.protocol.Message;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public interface IMReceiver {

    void receive(Message message);
}
