package com.hanson.im.client.handler;

import com.hanson.im.common.protocol.Message;

import java.util.concurrent.CompletableFuture;

/**
 * @author hanson
 * @Date 2019/1/15
 * @Description:
 */
public interface IMReplier {

    /**
     * to reply message
     * @param message
     * @return
     */
    CompletableFuture reply(Message message);
}
