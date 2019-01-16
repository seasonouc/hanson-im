package com.hanson.im.common.cryption;

import com.hanson.im.common.layer.HimSerializer;
import com.hanson.im.common.protocol.util.WriterUtil;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author hanson
 * @Date 2019/1/14
 * @Description:
 */
public class DiffPubKey implements HimSerializer {

    /**
     * g is the big integer according to the diff-hellman protocol
     */
    private BigInteger g;

    /**
     * p is the big prime integer according  to the diff-hellman protocol
     */
    private BigInteger p;

    public DiffPubKey() {
        Random random = new SecureRandom();
        g = new BigInteger(256, random);
        p = new BigInteger(512, 512, random);
    }


    public BigInteger getG() {
        return g;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }

    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    @Override
    public void writeTo(ByteBuf byteBuffer) {
        WriterUtil.writeBigInt(p, byteBuffer);
        WriterUtil.writeBigInt(g, byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) {
        p = WriterUtil.readBigInt(byteBuffer);
        g = WriterUtil.readBigInt(byteBuffer);
    }

}
