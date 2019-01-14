package com.hanson.im.common.cryption;

import com.hanson.im.common.exception.DecryptionException;
import com.hanson.im.common.exception.EncryptionException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;

/**
 * @author hanson
 * @Date 2019/1/13
 * @Description:
 */
public class CryptorTest {

    @Test
    public void testTowCombineKey(){

        Cryptor cryptor1 = new Cryptor();
        cryptor1.initPublicKey();
        cryptor1.initPrivateKey();
        BigInteger exKey1 = cryptor1.getExchangeKey();

        Cryptor cryptor2 = new Cryptor();
        cryptor2.initPrivateKey();
        cryptor2.setPubKey(cryptor1.getPubKey());
        BigInteger exKey2 = cryptor2.getExchangeKey();

        Assert.assertEquals(cryptor1.addExchangeKey(exKey2),cryptor2.addExchangeKey(exKey1));
    }

    @Test
    public void testThreeCombineKey(){
        Cryptor cryptor1 = new Cryptor();
        cryptor1.initPublicKey();
        cryptor1.initPrivateKey();
        BigInteger exKey1 = cryptor1.getExchangeKey();

        Cryptor cryptor2 = new Cryptor();
        cryptor2.initPrivateKey();
        cryptor2.setPubKey(cryptor1.getPubKey());
        BigInteger exKey2 = cryptor2.getExchangeKey();

        Cryptor cryptor3 = new Cryptor();
        cryptor3.initPrivateKey();
        cryptor3.setPubKey(cryptor1.getPubKey());
        BigInteger exKey3 = cryptor3.getExchangeKey();

        BigInteger key1 = cryptor1.addExchangeKey(cryptor2.addExchangeKey(exKey3));
        BigInteger key2 = cryptor2.addExchangeKey(cryptor1.addExchangeKey(exKey3));
        BigInteger key3 = cryptor3.addExchangeKey(cryptor2.addExchangeKey(exKey1));

        Assert.assertEquals(key1,key2);
        Assert.assertEquals(key2,key3);

        System.out.println(key1);

    }

    @Test
    public void testEncryptionAndDecryption() throws EncryptionException, DecryptionException {
        Cryptor cryptor1 = new Cryptor();
        cryptor1.initPublicKey();
        cryptor1.initPrivateKey();

        Cryptor cryptor2 = new Cryptor();
        cryptor2.initPrivateKey();
        cryptor2.setPubKey(cryptor1.getPubKey());
        BigInteger exKey2 = cryptor2.getExchangeKey();

        BigInteger key = cryptor1.addExchangeKey(exKey2);

        System.out.println(key);
        cryptor1.setCypherKey(key);
        byte[]  ebytes = cryptor1.encrypt("string".getBytes());

        byte[] dbytes = cryptor1.decrypt(ebytes);
        System.out.println(new String(dbytes));
        Assert.assertEquals(dbytes,"string".getBytes());
    }
}