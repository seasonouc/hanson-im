package com.hanson.im.client.handler;

import com.hanson.im.common.protocol.Message;

/**
 * @author hanson
 * @Date 2019/1/13
 * @Description:
 */
public interface IMSender {
    /**
     * use it to send message
     * @param message
     */
    public void send(Message message);
}
