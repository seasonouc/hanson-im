package com.hanson.im.server.service;

import com.hanson.im.common.protocol.Message;
import io.netty.channel.Channel;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public interface MessageHandler {


    /**
     *
     * @param channel
     * @param message
     */
    void handleMessage(Channel channel, Message message);
}
