package com.hanson.im.client.chat;

import com.hanson.im.client.ClientConfig;
import com.hanson.im.client.handler.IMReceiver;
import com.hanson.im.client.handler.IMSender;
import com.hanson.im.client.handler.MessageAcceptor;
import com.hanson.im.client.network.tcp.Connector;
import com.hanson.im.common.cryption.Cryptor;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.ExchangeEncryptKey;
import com.hanson.im.common.protocol.body.ExchangeKeySet;
import com.hanson.im.common.protocol.body.LoginRequest;
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
    public void buildEncryptChannle(List<String> userList) {
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
        Set<String> userSet = new HashSet<>(userList);
        keySet.put(cryptor.getExchangeKey(), userSet);

        exchangeKeySetList.add(exchangeKeySet);
        exchangeEncryptKey.setExchangeKeySetList(exchangeKeySetList);

        body.setData(exchangeEncryptKey, ExchangeEncryptKey.class);

        message.setHeader(header);
        message.setBody(body);
        imSender.send(message);
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

    public void sendMessage(String sessionId, String text) {

    }

    public void regiser(String userId, String userName) {

    }

    private String generateSessionId() {
        Long time = System.currentTimeMillis();
        Random random = new Random(100);
        return time.toString() + random.nextInt();
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
}
