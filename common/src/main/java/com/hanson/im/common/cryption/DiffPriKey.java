package com.hanson.im.common.cryption;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author hanson
 * @Date 2019/1/14
 * @Description:
 */
public class DiffPriKey {

    /**
     * the private key according to the diff-hellman protocol
     */
    private BigInteger priKey;

    public DiffPriKey(){
        priKey = new BigInteger(256,new SecureRandom());
    }

    protected BigInteger getPriKey(){
        return priKey;
    }
}
