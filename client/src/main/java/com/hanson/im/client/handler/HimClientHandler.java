package com.hanson.im.client.handler;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageHeader;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
public class HimClientHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * use it when receive message
     */
    private IMReceiver imReceiver;

    /**
     * the connecting channel with server
     */
    private Channel channel;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if(log.isDebugEnabled()){
            MessageHeader header = message.getHeader();
            log.debug("receive message from :{},message type:{}",header.getFrom(),header.getMessageType());
        }
        if(imReceiver == null){
            log.error("no message receiver have been set");
        }else{
            imReceiver.receive(message);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("built connection with server,send authentication message");
        channel = ctx.channel();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        log.error("lost connection with server");
        imReceiver.disconnect();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
    }



    public void setImReceiver(IMReceiver imReceiver){
        this.imReceiver = imReceiver;
    }

}
