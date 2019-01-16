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
public class ServerResponse implements HimSerializer {

    /**
     * the response content
     */
    private String content;

    /**
     * the status code
     */
    private int code;

    public ServerResponse(){

    }

    public ServerResponse(int code, String content){
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
    public void writeTo(ByteBuf byteBuffer)  {
        byteBuffer.writeInt(code);
        WriterUtil.writeString(content,byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        code = byteBuffer.readInt();
        content = WriterUtil.readString(byteBuffer);
    }
}
