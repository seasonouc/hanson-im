package com.hanson.im.common.protocol.body;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
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
    private String text;

    /**
     * the encrypt key version
     */
    private int version;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EncryptText() {

    }

    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        if (text == null) {
            byteBuffer.writeInt(0);
        } else {

            byte[] textBytes = text.getBytes();
            byteBuffer.writeInt(textBytes.length);

        }
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        int textLength = byteBuffer.readInt();
        byte[] textBytes = new byte[textLength];
        byteBuffer.readBytes(textBytes, 0, textLength);

        text = new String(textBytes);

    }
}
