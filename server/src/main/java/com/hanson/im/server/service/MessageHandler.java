package com.hanson.im.server.service;

import com.hanson.im.common.protocol.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;


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

    void userOffline(ChannelId channelId);
}
