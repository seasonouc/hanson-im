package com.hanson.im.common.layer;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public interface HimSerializer {
    /**
     * write object to bytes
     * @param byteBuffer
     */
    void writeTo(ByteBuf byteBuffer) throws EncodeException;

    /**
     * read byte data from byte code
     * @param byteBuffer
     */
    void readFrom(ByteBuf byteBuffer) throws DecodeException;
}
