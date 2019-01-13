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
        byte[] idBytes = userId.getBytes();
        byteBuffer.writeInt(idBytes.length);
        byteBuffer.writeBytes(idBytes);

        byte[] nameBytes = userId.getBytes();
        byteBuffer.writeInt(nameBytes.length);
        byteBuffer.writeBytes(nameBytes);


    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        int idLength = byteBuffer.readInt();
        byte[] idBytes = new byte[idLength];
        byteBuffer.readBytes(idBytes, 0, idLength);

        userId = new String(idBytes);

        int nameLength = byteBuffer.readInt();
        byte[] nameBytes = new byte[nameLength];
        byteBuffer.readBytes(nameBytes, 0, nameLength);
        userName = new String(nameBytes);

    }
}
