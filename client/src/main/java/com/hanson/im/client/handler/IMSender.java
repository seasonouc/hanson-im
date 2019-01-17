package com.hanson.im.client.handler;

import com.hanson.im.common.protocol.Message;

import java.util.concurrent.CompletableFuture;

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
    CompletableFuture<Boolean> send(Message message);
}
