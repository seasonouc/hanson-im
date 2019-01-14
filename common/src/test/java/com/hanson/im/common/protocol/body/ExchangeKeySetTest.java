package com.hanson.im.common.protocol.body;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.testng.Assert;
import org.testng.annotations.Test;

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
public class ExchangeKeySetTest {


    @Test
    public void testWriteAndRead() throws EncodeException, DecodeException {
        ExchangeKeySet exchangeKeySet = new ExchangeKeySet();

        Map<BigInteger,Set<String>> exchangeKeyCache = new HashMap<>();

        Set<String> set = new HashSet<>();
        set.add("123");
        set.add("456");

        exchangeKeyCache.put(new BigInteger("123456"),set);
        exchangeKeyCache.put(new BigInteger("123456"),set);
        exchangeKeyCache.put(new BigInteger("123478"),set);

        exchangeKeySet.setExChangeKeyCache(exchangeKeyCache);

        ByteBuf byteBuf = Unpooled.buffer(2048);

        exchangeKeySet.writeTo(byteBuf);

        ExchangeKeySet exchangeKeySet1 = new ExchangeKeySet();
        exchangeKeySet1.readFrom(byteBuf);

        System.out.println(exchangeKeyCache.size());
        Assert.assertEquals(exchangeKeySet1.getExChangeKeyCache().size(),exchangeKeyCache.size());

        for(Map.Entry<BigInteger,Set<String>> entry:exchangeKeyCache.entrySet()){
            Assert.assertEquals(entry.getValue(),exchangeKeySet1.getExChangeKeyCache().get(entry.getKey()));
        }

    }

}