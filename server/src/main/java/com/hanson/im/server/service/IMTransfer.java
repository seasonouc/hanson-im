package com.hanson.im.server.service;

import com.hanson.im.common.protocol.Message;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;


/**
 * @author hanson
 * @Date 2019/1/16
 * @Description:
 */
public interface IMTransfer {

    CompletableFuture<Boolean> sendMessage(String userId, Message message);

    void addChannel(String userId, Channel channel);
}
