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
public class LoginRequest implements HimSerializer {

    /**
     * user id
     */
    private String userId;
    /**
     * user name
     */
    private String userName;

    public LoginRequest(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        WriterUtil.writeString(userId,byteBuffer);
        WriterUtil.writeString(userName,byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        userId = WriterUtil.readString(byteBuffer);
        userName = WriterUtil.readString(byteBuffer);

    }
}
