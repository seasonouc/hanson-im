package com.hanson.im.common.cryption;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author hanson
 * @Date 2019/1/14
 * @Description:
 */
public class DiffPubKey implements HimSerializer{

    /**
     * g is the big integer according to the diff-hellman protocol
     */
    private BigInteger g;

    /**
     * p is the big prime integer according  to the diff-hellman protocol
     */
    private BigInteger p;

    public DiffPubKey(){
        Random random = new SecureRandom();
        g = new BigInteger(256,random);
        p = new BigInteger(512,512,random);
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
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        byte[] bytesP =  p.toByteArray();
        byteBuffer.writeInt(bytesP.length);
        byteBuffer.writeBytes(bytesP);

        byte[] bytesG =  g.toByteArray();
        byteBuffer.writeInt(bytesG.length);
        byteBuffer.writeBytes(bytesG);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {

    }

}
