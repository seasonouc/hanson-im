package com.hanson.im.client.chat;

import com.hanson.im.client.ClientConfig;
import com.hanson.im.client.handler.IMSender;
import com.hanson.im.common.cryption.Cryptor;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.EncryptText;
import com.hanson.im.common.protocol.body.ExchangeEncryptKey;
import com.hanson.im.common.protocol.body.ExchangeKeySet;

import java.math.BigInteger;
import java.util.*;

/**
 * @author hanson
 * @Date 2019/1/14
 * @Description:
 */
public class Controller {

    /**
     * use it to send message
     */
    private IMSender imSender;

    private String myId;

    private String myName;

    /**
     * store the chat user set in one chat
     */
    private Map<String,Set<String>> chatCache;

    /**
     * store the cryptor in one chat
     */
    private Map<String,Cryptor> cryptorCache;

    public Controller(IMSender imSender){

        this.imSender = imSender;
        chatCache = new HashMap<>();

    }

    /**
     * build the encrypt communication channel between few users
     * @param userList
     */
    public void buildEncryptChannle(List<String> userList){
        Message message = new Message();

        MessageHeader header = new MessageHeader();
        header.setVersion(ClientConfig.version);
        header.setToList(userList);
        header.setMessageType(MessageType.KEY_EXCHANGE_TO);
        header.setFrom(myId);

        MessageBody body = new MessageBody();
        ExchangeEncryptKey exchangeEncryptKey = new ExchangeEncryptKey();

        Cryptor cryptor = new Cryptor();
        cryptor.initPublicKey();
        cryptor.initPrivateKey();

        exchangeEncryptKey.setDiffPubKey(cryptor.getPubKey());
        List<ExchangeKeySet> exchangeKeySetList = new ArrayList<>();

        ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
        Map<BigInteger,Set<String>> keySet = new HashMap<>();
        Set<String> userSet = new HashSet<>(userList);
        keySet.put(cryptor.getExchangeKey(),userSet);

        exchangeKeySetList.add(exchangeKeySet);
        exchangeEncryptKey.setExchangeKeySetList(exchangeKeySetList);

        body.setData(exchangeEncryptKey,ExchangeEncryptKey.class);

        message.setHeader(header);
        message.setBody(body);

        imSender.send(message);
    }

    public void sendText(String text,List<String> list){
        Message message = new Message();

        MessageHeader header = new MessageHeader();
        header.setVersion(ClientConfig.version);
        header.setFrom(myId);
        header.setMessageType(MessageType.ENCRYPT_TEXT_MSG);
        header.setToList(list);
        message.setHeader(header);

        MessageBody body = new MessageBody();
        EncryptText encryptText =  new EncryptText();
        encryptText.setText(text);
        body.setData(encryptText,EncryptText.class);
        message.setBody(body);

        imSender.send(message);
    }

    public void sendMessage(String groupId){

    }

    public void login(){

    }
}
