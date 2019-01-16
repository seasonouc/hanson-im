package com.hanson.im.server.server.handler;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.server.service.IMTransfer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hanson
 * @Date 2019/1/16
 * @Description:
 */
@Slf4j
@Service
public class MessageTransfer implements IMTransfer{


    private Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Boolean> sendMessage(String userId, Message message) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        if(channelMap.containsKey(userId)){
            Channel channel = channelMap.get(userId);
            channel.writeAndFlush(message);
        }
        return future;
    }

    @Override
    public void addChannel(String userId, Channel channel) {
        channelMap.put(userId,channel);
    }


}
