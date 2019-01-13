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
public class NormalResponse implements HimSerializer {

    /**
     * the response content
     */
    private String content;

    /**
     * the status code
     */
    private int code;

    public NormalResponse(){

    }

    public NormalResponse(int code,String content){
        this.code = code;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getCode(){
        return code;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        byteBuffer.writeInt(code);

        byte[] contenByte = content.getBytes();
        byteBuffer.writeInt(contenByte.length);
        byteBuffer.writeBytes(contenByte);

    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        code = byteBuffer.readInt();

        int contentLength = byteBuffer.readInt();
        byte[] bytes = new byte[contentLength];
        byteBuffer.readBytes(bytes, 0, contentLength);
        content = new String(bytes );
    }
}
