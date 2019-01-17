package com.hanson.im.client.chat;

import com.hanson.im.client.ClientConfig;
import com.hanson.im.client.handler.BuildChannelListenner;
import com.hanson.im.client.handler.IMReceiver;
import com.hanson.im.client.handler.IMSender;
import com.hanson.im.client.handler.MessageAcceptor;
import com.hanson.im.client.network.tcp.Connector;
import com.hanson.im.common.cryption.Cryptor;
import com.hanson.im.common.exception.EncryptionException;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.EncryptText;
import com.hanson.im.common.protocol.body.ExchangeEncryptKey;
import com.hanson.im.common.protocol.body.ExchangeKeySet;
import com.hanson.im.common.protocol.body.LoginRequest;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author hanson
 * @Date 2019/1/14
 * @Description:
 */
@Slf4j
public class Controller implements IMSender, IStatus {

    /**
     * use it to send message
     */
    private IMSender imSender;

    private String myId;

    private String myName;

    private boolean login;

    /**
     * http server address
     */
    private String httpServerAddr;

    private InetSocketAddress address;
    /**
     * store the chat user set in one chat
     */
    private Map<String, Set<String>> chatCache;

    /**
     * store the cryptor in one chat
     */
    private Map<String, Cryptor> cryptorCache;

    /**
     * tcp network connector
     */
    private Connector connector;

    /**
     * message receiver
     */
    private IMReceiver imReceiver;

    /**
     * build channel listener
     */
    private BuildChannelListenner listenner;

    public Controller() {

        chatCache = new HashMap<>();
        cryptorCache = new HashMap<>();
        imReceiver = new MessageAcceptor(this, this, chatCache, cryptorCache);
        connector = new Connector(imReceiver, chatCache, cryptorCache);
        this.imSender = connector;
    }

    public void setHttpServerAddr(String httpServerAddr) {
        this.httpServerAddr = httpServerAddr;
    }

    public void setInetSocketAddress(String ip, int port) {
        address = new InetSocketAddress(ip, port);
    }

    public void connect() {
        connector.start(address).whenComplete((result, error) -> {
            if (result) {
                log.info("connecting the server success");
            } else {
                log.error("connection the server failed");
            }
        });
    }

    public void setIdAndName(String myId, String myName) {
        this.myId = myId;
        this.myName = myName;
    }

    /**
     * build the encrypt communication channel between few users
     *
     * @param userList
     */
    public CompletableFuture<Boolean> buildEncryptChannle(List<String> userList) {
        CompletableFuture<String> future = new CompletableFuture<>();
        log.info("build encrypt channel with {}",userList);
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

        String sessionId = generateSessionId();

        cryptorCache.put(sessionId, cryptor);

        exchangeEncryptKey.setDiffPubKey(cryptor.getPubKey());
        exchangeEncryptKey.setSessionId(sessionId);
        List<ExchangeKeySet> exchangeKeySetList = new ArrayList<>();

        ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
        Map<BigInteger, Set<String>> keySet = new HashMap<>();
        Set<String> userSet = new HashSet<>();
        userSet.add(myId);
        keySet.put(cryptor.getExchangeKey(), userSet);
        exchangeKeySet.setExChangeKeyCache(keySet);

        exchangeKeySetList.add(exchangeKeySet);
        exchangeEncryptKey.setExchangeKeySetList(exchangeKeySetList);

        Set<String> joinSet = new HashSet<>(userList);
        joinSet.add(myId);
        exchangeEncryptKey.setJoinSet(joinSet);

        body.setData(exchangeEncryptKey, ExchangeEncryptKey.class);

        message.setHeader(header);
        message.setBody(body);
        return imSender.send(message);
    }


    public CompletableFuture<Boolean> login() {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUserName(myName);
        loginRequest.setUserId(myId);

        Message message = new Message();
        MessageHeader header = new MessageHeader();
        header.setVersion(ClientConfig.version);
        header.setFrom(loginRequest.getUserId());
        header.setToList(null);
        header.setMessageType(MessageType.LOGIN_TO);

        message.setHeader(header);

        MessageBody body = new MessageBody();
        body.setData(loginRequest, LoginRequest.class);
        message.setBody(body);
        return imSender.send(message);
    }

    public CompletableFuture<Boolean> sendMessage(String sessionId, String text) {
        if(chatCache.containsKey(sessionId)){
            return new CompletableFuture<>();
        }
        Set<String> set = new HashSet<>(chatCache.get(sessionId));
        if(set.size() == 0){
            return new CompletableFuture<>();
        }
        set.remove(myId);
        Message message = new Message();
        MessageHeader header = new MessageHeader();
        header.setToList(new ArrayList<>(set));
        header.setVersion(ClientConfig.version);
        header.setFrom(myId);
        header.setMessageType(MessageType.ENCRYPT_TEXT_MSG);
        message.setHeader(header);

        MessageBody body = new MessageBody();
        EncryptText encryptText = new EncryptText();
        Cryptor cryptor = cryptorCache.get(sessionId);
        try {
            encryptText.setContent(cryptor.encryptString(text));
        } catch (EncryptionException e) {
            log.error("encrypt message error:{}",e.getMessage());
        }
        encryptText.setSessionId(sessionId);
        body.setData(encryptText,EncryptText.class);
        message.setBody(body);

        return imSender.send(message);
    }

    public void regiser(String userId, String userName) {

    }

    private String generateSessionId() {
        Long time = System.currentTimeMillis();
        Random random = new Random(100);
        return time.toString() + random.nextInt();
    }

    public void setListenner(BuildChannelListenner listenner){
        this.listenner = listenner;
    }

    @Override
    public CompletableFuture send(Message message) {
        return imSender.send(message);
    }

    @Override
    public void setLogin(boolean login) {
        this.login = login;
    }

    @Override
    public boolean getLogin() {
        return login;
    }

    @Override
    public String getMyId() {
        return myId;
    }

    @Override
    public String getMyName() {
        return myName;
    }

    @Override
    public void buildChannel(String userId) {
        this.listenner.buildChannle(userId);
    }
}
