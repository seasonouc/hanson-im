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
public class LoginResponse implements HimSerializer {

    /**
     * the response content
     */
    private UserInfo userInfo;

    /**
     * the status code
     */
    private int code;

    public LoginResponse(){

    }

    public LoginResponse(int code, UserInfo userInfo){
        this.code = code;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public int getCode(){
        return code;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    @Override
    public void writeTo(ByteBuf byteBuffer)  {
        byteBuffer.writeInt(code);
       userInfo.writeTo(byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) {
        code = byteBuffer.readInt();
        userInfo = new UserInfo();
        userInfo.readFrom(byteBuffer);
    }
}
