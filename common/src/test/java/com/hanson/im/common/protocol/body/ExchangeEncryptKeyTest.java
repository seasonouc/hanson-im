package com.hanson.im.common.protocol.body;

import com.hanson.im.common.cryption.Cryptor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.*;

import static org.testng.Assert.*;

/**
 * @author hanson
 * @Date 2019/1/16
 * @Description:
 */
public class ExchangeEncryptKeyTest {

    @Test
    public void testWriteAndRead(){
        ExchangeEncryptKey exchangeEncryptKey = new ExchangeEncryptKey();
        ByteBuf byteBuf = Unpooled.buffer(2048);

        Set<String> joinSet = new HashSet<>();
        joinSet.add("123");
        joinSet.add("456");

        exchangeEncryptKey.setJoinSet(joinSet);

        Cryptor cryptor = new Cryptor();
        cryptor.initPublicKey();
        exchangeEncryptKey.setDiffPubKey(cryptor.getPubKey());
        String sessionId = UUID.randomUUID().toString();
        exchangeEncryptKey.setSessionId(sessionId);


        List<ExchangeKeySet> list = new ArrayList<>();
        ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
        Map<BigInteger,Set<String>> exchangeKeyCache = new HashMap<>();
        Set<String> set = new HashSet<>();
        set.add("123");
        set.add("456");
        exchangeKeyCache.put(new BigInteger("123456"),set);
        exchangeKeyCache.put(new BigInteger("123478"),set);
        exchangeKeySet.setExChangeKeyCache(exchangeKeyCache);
        list.add(exchangeKeySet);

        exchangeEncryptKey.setExchangeKeySetList(list);

        exchangeEncryptKey.writeTo(byteBuf);

        ExchangeEncryptKey inputKey = new ExchangeEncryptKey();
        inputKey.readFrom(byteBuf);

        Assert.assertEquals(exchangeEncryptKey.getSessionId(),inputKey.getSessionId());
        Assert.assertEquals(exchangeEncryptKey.getDiffPubKey().getP(),inputKey.getDiffPubKey().getP());
        Assert.assertEquals(exchangeEncryptKey.getDiffPubKey().getG(),inputKey.getDiffPubKey().getG());
        Assert.assertEquals(exchangeEncryptKey.getJoinSet(),inputKey.getJoinSet());

        System.out.println(inputKey.getJoinSet());
    }

}