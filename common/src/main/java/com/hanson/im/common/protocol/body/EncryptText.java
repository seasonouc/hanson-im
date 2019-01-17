package com.hanson.im.common.protocol.body;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import com.hanson.im.common.protocol.util.WriterUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class EncryptText implements HimSerializer {
    /**
     * the encrypt text send or received
     */
    private byte[] content;

    /**
     * the encrypt key version
     */
    private int version;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private String sessionId;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public EncryptText() {

    }

    @Override
    public void writeTo(ByteBuf byteBuffer)  {
        WriterUtil.writeString(sessionId,byteBuffer);
        byteBuffer.writeInt(content.length);
        byteBuffer.writeBytes(content);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) {
        sessionId = WriterUtil.readString(byteBuffer);

    }
}
