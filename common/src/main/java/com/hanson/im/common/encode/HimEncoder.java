package com.hanson.im.common.encode;

import com.hanson.im.common.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;


/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
public class HimEncoder extends MessageToByteEncoder<Message>{


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(0);
        message.writeTo(byteBuf);
        int length = byteBuf.readableBytes();
        byteBuf.resetWriterIndex();
        byteBuf.writeInt(length - 4);
        byteBuf.setIndex(0,length);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
    }
}
