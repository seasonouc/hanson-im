package com.hanson.im.common.protocol.body;

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
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EncryptText() {

    }

    @Override
    public void writeTo(ByteBuf byteBuffer)  {
        WriterUtil.writeString(sessionId,byteBuffer);
        WriterUtil.writeString(content,byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) {
        sessionId = WriterUtil.readString(byteBuffer);
        content = WriterUtil.readString(byteBuffer);
    }
}
