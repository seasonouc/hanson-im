package com.hanson.im.common.decode;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Slf4j
public class HimDecoder extends ByteToMessageDecoder{

    private int BASE_LENGTH = 8;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws DecodeException {
        if(byteBuf.readableBytes() > BASE_LENGTH){
            int length = byteBuf.getInt(byteBuf.readerIndex());

            //to solve the tcp half package problem
            if(byteBuf.readableBytes() < length + 4){
                return;
            }
            byteBuf.skipBytes(4);

            Message message = new Message();
            message.readFrom(byteBuf);

            list.add(message);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("decode caught error:{}",cause.getMessage());
        ctx.close();
    }
}
