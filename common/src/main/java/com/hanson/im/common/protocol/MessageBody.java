package com.hanson.im.common.protocol;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import com.hanson.im.common.protocol.body.EncryptText;
import com.hanson.im.common.protocol.body.ExchangeEncryptKey;
import com.hanson.im.common.protocol.body.LoginRequest;
import com.hanson.im.common.protocol.body.NormalResponse;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


/**
 * @author hanson
 * @Date 2019/1/10
 * @Description:
 */
public class MessageBody implements HimSerializer {

    public static final Map<BodyType, Class<? extends HimSerializer>> bodyTypeClassMap = new HashMap();
    public static final Map<Class<? extends HimSerializer>,BodyType> classBodyTypeMap = new HashMap();

    static {
        bodyTypeClassMap.put(BodyType.EXCHENGE_KEY, ExchangeEncryptKey.class);
        bodyTypeClassMap.put(BodyType.ENCRYPT_TEXT, EncryptText.class);
        bodyTypeClassMap.put(BodyType.LOING_IN, LoginRequest.class);
        bodyTypeClassMap.put(BodyType.NORMAL_RESPONSE, NormalResponse.class);
    }

    static {
        classBodyTypeMap.put(ExchangeEncryptKey.class,BodyType.EXCHENGE_KEY );
        classBodyTypeMap.put(EncryptText.class,BodyType.ENCRYPT_TEXT);
        classBodyTypeMap.put(LoginRequest.class,BodyType.LOING_IN);
        classBodyTypeMap.put(NormalResponse.class,BodyType.NORMAL_RESPONSE);
    }

    private Object data;
    private BodyType bodyType;

    public MessageBody(){

    }

    public Object getData(){
        return data;
    }

    public void setData(Object data,Class clazz){
        this.data = data;
        bodyType = classBodyTypeMap.get(clazz);
    }

    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        byteBuffer.writeInt(bodyType.ordinal());
        ((HimSerializer)data).writeTo(byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        int bodyTypeOrder = byteBuffer.readInt();
        if (bodyTypeOrder < 0 || bodyTypeOrder >= BodyType.values().length) {
            throw new DecodeException("body type is out of range");
        }
        bodyType = BodyType.values()[bodyTypeOrder];
        try {
            Class<? extends HimSerializer> clazz = bodyTypeClassMap.get(bodyType);
            data = Class.forName(clazz.getName()).newInstance();
            ((HimSerializer)data).readFrom(byteBuffer);
        } catch (InstantiationException e) {
            throw new DecodeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new DecodeException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new DecodeException(e.getMessage());
        }

    }

    public enum BodyType {
        EXCHENGE_KEY,
        ENCRYPT_TEXT,
        ENCRYPT_PICTURE,
        LOING_IN,
        NORMAL_RESPONSE
    }
}
