package com.hanson.im.common.protocol;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import io.netty.buffer.ByteBuf;

/**
 * @author hanson
 * @Date 2019/1/10
 * @Description:
 */
public class Message implements HimSerializer{

    public static final String encoding = "utf-8";

    public static final float version = 1.0f;

    /**
     * the request body
     */
    private MessageBody body;
    /**
     * the request header
     */
    private MessageHeader header;

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }




    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        header.writeTo(byteBuffer);
        body.writeTo(byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        header = new MessageHeader();
        header.readFrom(byteBuffer);

        body = new MessageBody();
        body.readFrom(byteBuffer);
    }
}
