package com.hanson.im.common.protocol.body;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author hanson
 * @Date 2019/1/14
 * @Description:
 */
public class ExchangeKeySet implements HimSerializer {

    /**
     * to cache the exchange key in the middle process of the protocol
     */
    private Map<BigInteger, Set<String>> exChangeKeyCache;

    public Map<BigInteger, Set<String>> getExChangeKeyCache() {
        return exChangeKeyCache;
    }

    public void setExChangeKeyCache(Map<BigInteger, Set<String>> exChangeKeyCache) {
        this.exChangeKeyCache = exChangeKeyCache;
    }

    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        byteBuffer.writeInt(exChangeKeyCache.size());
        for (Map.Entry<BigInteger, Set<String>> entry : exChangeKeyCache.entrySet()) {
            byte[] keyBytes = entry.getKey().toByteArray();
            byteBuffer.writeInt(keyBytes.length);
            byteBuffer.writeBytes(keyBytes);
            byteBuffer.writeInt(entry.getValue().size());
            entry.getValue().forEach(userId -> {
                byte[] userBytes = userId.getBytes();
                byteBuffer.writeInt(userBytes.length);
                byteBuffer.writeBytes(userBytes);
            });
        }
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        exChangeKeyCache = new HashMap<>();
        int mapSize = byteBuffer.readInt();
        for (int i = 0; i < mapSize; i++) {
            int keyLength = byteBuffer.readInt();
            byte[] keyByte = new byte[keyLength];
            byteBuffer.readBytes(keyByte, 0, keyLength);
            BigInteger key = new BigInteger(keyByte);

            Set<String> userIds = new HashSet<>();
            int usersSize = byteBuffer.readInt();
            for (int j = 0; j < usersSize; j++) {
                int userLength = byteBuffer.readInt();
                byte[] userBytes = new byte[userLength];
                byteBuffer.readBytes(userBytes, 0, userLength);
                String userId = new String(userBytes);
                userIds.add(userId);
                exChangeKeyCache.put(key,userIds);
            }
        }
    }
}
