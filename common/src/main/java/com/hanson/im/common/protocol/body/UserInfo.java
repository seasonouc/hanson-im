package com.hanson.im.common.protocol.body;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import com.hanson.im.common.protocol.util.WriterUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
public class UserInfo implements HimSerializer {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String id;
    private String userName;

    @Override
    public void writeTo(ByteBuf byteBuffer) {
        WriterUtil.writeString(id,byteBuffer);
        WriterUtil.writeString(userName,byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) {
        id = WriterUtil.readString(byteBuffer);
        userName = WriterUtil.readString(byteBuffer);
    }
}
