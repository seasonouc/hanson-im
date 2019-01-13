package com.hanson.im.server.server.handler;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.server.service.MessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Slf4j
@ChannelHandler.Sharable
public class HimServerChannelHandler extends SimpleChannelInboundHandler<Message>{


    private MessageHandler handler;

    public HimServerChannelHandler(MessageHandler handler){
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Message message)  {
        handler.handleMessage(context.channel(),message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("built a connection  with a client");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        log.error("exception caught error:{}",cause.getMessage());
        ctx.close();
    }

}
