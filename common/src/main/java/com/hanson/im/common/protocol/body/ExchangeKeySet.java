package com.hanson.im.common.protocol.body;

import com.hanson.im.common.layer.HimSerializer;
import com.hanson.im.common.protocol.util.WriterUtil;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.util.*;

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
    public void writeTo(ByteBuf byteBuffer) {
        byteBuffer.writeInt(exChangeKeyCache.size());
        for (Map.Entry<BigInteger, Set<String>> entry : exChangeKeyCache.entrySet()) {
            WriterUtil.writeBigInt(entry.getKey(),byteBuffer);
            WriterUtil.writeSetString(entry.getValue(),byteBuffer);
        }
    }

    @Override
    public void readFrom(ByteBuf byteBuffer)  {
        exChangeKeyCache = new HashMap<>();
        int mapSize = byteBuffer.readInt();
        for (int i = 0; i < mapSize; i++) {
            BigInteger key = WriterUtil.readBigInt(byteBuffer);
            Set<String> userIds = WriterUtil.readSetString(byteBuffer);
            exChangeKeyCache.put(key,userIds);
        }
    }
}
