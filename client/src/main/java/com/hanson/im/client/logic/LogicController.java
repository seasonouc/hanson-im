package com.hanson.im.client.logic;

import com.hanson.im.client.config.ClientConfig;
import com.hanson.im.client.handler.BuildChannelListenner;
import com.hanson.im.client.handler.IMReceiver;
import com.hanson.im.client.handler.IMSender;
import com.hanson.im.client.handler.MessageAcceptor;
import com.hanson.im.client.network.tcp.Connector;
import com.hanson.im.client.ui.base.ShowMessage;
import com.hanson.im.common.cryption.Cryptor;
import com.hanson.im.common.exception.EncryptionException;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.*;
import com.hanson.im.common.utils.HashUtil;
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
public class LogicController implements IMSender, IStatus {

    private static LogicController controller = new LogicController();

    public static LogicController getController() {
        return controller;
    }

    /**
     * use it to send message
     */
    private IMSender imSender;

    /**
     * my user id
     */
    private String myId;

    /**
     * my user name
     */
    private String myName;

    /**
     * login status
     */
    private boolean login;

    /**
     *
     */
    private boolean connected;

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
     * cache the hash session
     */
    private Map<String, String> hashToSession;

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

    private EventListener eventListener;


    public LogicController() {

        hashToSession = new HashMap<>();
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
                connected = true;
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
    public CompletableFuture<Boolean> buildEncryptChannle(List<UserInfo> userList) {
        log.info("build encrypt channel with {}", userList);
        if (userList == null | userList.size() == 0) {
            return new CompletableFuture<>();
        }
        String hash = HashUtil.getHashUtil().md5(userList);
        if (hashToSession.containsKey(hash)) {
            return new CompletableFuture<>();
        }

        String sessionId = generateSessionId();
        hashToSession.put(hash, sessionId);

        Message message = new Message();

        MessageHeader header = new MessageHeader();
        header.setVersion(ClientConfig.version);
        List<String> idList = new ArrayList<>();
        userList.forEach(userInfo -> idList.add(userInfo.getId()));
        header.setToList(idList);
        header.setMessageType(MessageType.KEY_EXCHANGE_TO);
        header.setFrom(myId);

        MessageBody body = new MessageBody();
        ExchangeEncryptKey exchangeEncryptKey = new ExchangeEncryptKey();

        Cryptor cryptor = new Cryptor();
        cryptor.initPublicKey();
        cryptor.initPrivateKey();

        StringBuilder nameSb = new StringBuilder();
        for (int i = 0; i < userList.size() & i < 3; i++) {
            nameSb.append(userList.get(i).getUserName());
            nameSb.append("&");
        }
        nameSb.append(myName);
        exchangeEncryptKey.setName(nameSb.toString());
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

        Set<String> joinSet = new HashSet<>(idList);
        joinSet.add(myId);
        exchangeEncryptKey.setJoinSet(joinSet);

        body.setData(exchangeEncryptKey, ExchangeEncryptKey.class);

        message.setHeader(header);
        message.setBody(body);
        return imSender.send(message);
    }


    public CompletableFuture<Boolean> login(String userId,String password) {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setPassword(password);
        loginRequest.setUserId(userId);

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

        Set<String> set = chatCache.get(sessionId);
        if (set == null || set.size() == 0) {
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            future.complete(false);
            return future;
        }
        Set<String> sendSet = new HashSet<>(set);
        sendSet.remove(myId);
        Message message = new Message();
        MessageHeader header = new MessageHeader();
        header.setToList(new ArrayList<>(sendSet));
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
            log.error("encrypt message error:{}", e.getMessage());
        }
        encryptText.setSessionId(sessionId);
        encryptText.setUserName(myName);
        body.setData(encryptText, EncryptText.class);
        message.setBody(body);

        ShowMessage showMessage = new ShowMessage();
        showMessage.setContent(text);
        showMessage.setSenderId(sessionId);
        showMessage.setSenderId(myId);
        showMessage.setUserName(myName);

        MessageCache.getMessageCache().addMeesage(sessionId, showMessage);

        return imSender.send(message);
    }

    public void regiser(String userId, String userName) {

    }

    public String generateSessionId() {
        Long time = System.currentTimeMillis();
        int randNumber = (int) (Math.random() * 1000);
        return time.toString() + randNumber;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setListenner(BuildChannelListenner listenner) {
        this.listenner = listenner;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
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
    public void buildChannel(UserInfo userInfo) {
        eventListener.buildChannelCall(userInfo);
    }

    @Override
    public void loginBack(boolean result, int code,UserInfo userInfo) {
        if (result) {
            log.info("login success");
        } else {
            log.info("login failed");
        }
        setIdAndName(userInfo.getId(),userInfo.getUserName());
        eventListener.loginCall(result);
    }

    @Override
    public void disconnect() {
        log.error("disconnect with server call back");
    }



    @Override
    public void receiveMessage(Message message) {
        eventListener.receivCall(message);
    }

    public void logout(){
        connector.logout();
    }
}
