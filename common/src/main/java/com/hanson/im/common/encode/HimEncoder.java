package com.hanson.im.common.encode;

import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Slf4j
public class HimEncoder extends MessageToByteEncoder<Message>{


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) {
        byteBuf.writeInt(0);

        try {
            message.writeTo(byteBuf);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        int length = byteBuf.readableBytes();
        byteBuf.resetWriterIndex();
        byteBuf.writeInt(length - 4);
        byteBuf.setIndex(0,length);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("encode caught error:{}",cause.getStackTrace());
        ctx.close();
    }
}
