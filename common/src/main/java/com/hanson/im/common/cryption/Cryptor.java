package com.hanson.im.common.cryption;

import com.hanson.im.common.exception.DecryptionException;
import com.hanson.im.common.exception.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author hanson
 * @Date 2019/1/13
 * @Description: 1.this class is used to combine a diff-hellman secret key
 * 2.use it to encrypt or decrypt a message
 */
public class Cryptor {

    /**
     * private key
     */
    private DiffPriKey priKey;

    /**
     * public key
     */
    private DiffPubKey pubKey;


    /**
     * the key to encrypt the message and decrypt the message
     */
    private transient BigInteger cypherKey;


    /**
     * encipher
     */
    private transient Cipher encryptCipher;

    /**
     * decipher
     */
    private transient Cipher decryptCipher;

    /**
     * initiate you private exchange key
     */
    public void initPrivateKey() {
        priKey = new DiffPriKey();
    }

    /**
     * initiate you public exchange key
     */
    public void initPublicKey() {
        pubKey = new DiffPubKey();
    }

    public DiffPubKey getPubKey() {
        return pubKey;
    }

    /**
     * @param pubKey
     */
    public void setPubKey(DiffPubKey pubKey) {
        this.pubKey = pubKey;
    }

    public BigInteger getCypherKey() {
        return cypherKey;
    }

    public void setCypherKey(BigInteger cypherKey) {
        this.cypherKey = cypherKey;
        SecureRandom random = new SecureRandom(cypherKey.toByteArray());
        try {

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] encodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");
            encryptCipher = Cipher.getInstance("AES");
            decryptCipher = Cipher.getInstance("AES");

            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    /**
     * get initial  exchange key with others
     *
     * @return
     */
    public BigInteger getExchangeKey() {
        return pubKey.getG().modPow(priKey.getPriKey(), pubKey.getP());
    }

    public BigInteger addExchangeKey(BigInteger exChangeKey) {
        return exChangeKey.modPow(priKey.getPriKey(), pubKey.getP());
    }

    /**
     * encrypt the plain message
     *
     * @param plainMessage
     * @return
     */
    public byte[] encrypt(byte[] plainMessage) throws EncryptionException {
        try {
            return encryptCipher.doFinal(plainMessage);
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    /**
     * encrypt text message
     * @param text
     * @return
     * @throws EncryptionException
     */
    public byte[] encryptString(String text) throws EncryptionException {
        return encrypt(text.getBytes());
    }

    /**
     * decrypt string message
     * @param cipher
     * @return
     * @throws DecryptionException
     */
    public String decryptString(byte[] cipher) throws DecryptionException {
        byte[] bytes = decrypt(cipher);
        return new String(bytes);
    }

    /**
     * decrypt the encrypted message
     *
     * @param encryptMessage
     * @return
     */
    public byte[] decrypt(byte[] encryptMessage) throws DecryptionException {
        try {
            return decryptCipher.doFinal(encryptMessage);
        } catch (Exception e) {
            throw new DecryptionException(e.getMessage());
        }
    }
}
