package com.hanson.im.common.protocol.body;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class ExchangeEncryptKey implements HimSerializer{

    /**
     * diff-hellman exchange key
     */
    private BigInteger exchangeKey;

    /**
     * the encrypt version
     */
    private int version;

    public ExchangeEncryptKey(){

    }

    public BigInteger getExchangeKey() {
        return exchangeKey;
    }

    public void setExchangeKey(BigInteger exchangeKey) {
        this.exchangeKey = exchangeKey;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        byte[] keyBytes = exchangeKey.toByteArray();
        byteBuffer.writeInt(keyBytes.length);
        byteBuffer.writeBytes(keyBytes);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        int keyLength = byteBuffer.readInt();
        byte[] keyBytes = new byte[keyLength];
        byteBuffer.readBytes(keyBytes,0,keyLength);
        exchangeKey = new BigInteger(keyBytes);
    }
}
