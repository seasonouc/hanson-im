package com.hanson.im.server.server.initializer;

import com.hanson.im.common.decode.HimDecoder;
import com.hanson.im.common.encode.HimEncoder;
import com.hanson.im.server.server.handler.HimServerChannelHandler;
import com.hanson.im.server.service.MessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Component
public class HimServerInitializer extends ChannelInitializer<Channel> {

    @Autowired
    MessageHandler handler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast( new HimDecoder())
                .addLast( new HimEncoder())
                .addLast(new HimServerChannelHandler(handler));

    }
}
