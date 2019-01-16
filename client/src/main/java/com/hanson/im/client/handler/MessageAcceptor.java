package com.hanson.im.client.handler;

import com.hanson.im.client.ClientConfig;
import com.hanson.im.client.chat.IStatus;
import com.hanson.im.common.cryption.Cryptor;
import com.hanson.im.common.protocol.*;
import com.hanson.im.common.protocol.body.EncryptText;
import com.hanson.im.common.protocol.body.ExchangeEncryptKey;
import com.hanson.im.common.protocol.body.ExchangeKeySet;
import com.hanson.im.common.protocol.body.ServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.*;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
public class MessageAcceptor implements IMReceiver {

    /**
     * my user id
     */
    private String myId;

    /**
     * store the chat user set in one chat
     */
    private Map<String, Set<String>> chatCache;

    /**
     * store the cryptor in one chat
     */
    private Map<String, Cryptor> cryptorCache;

    private IMSender imSender;

    private IStatus iStatus;

    public MessageAcceptor(IStatus iStatus,IMSender imSender, Map<String, Set<String>> chatCache, Map<String, Cryptor> cryptorCache) {
        this.iStatus = iStatus;
        this.imSender = imSender;
        this.chatCache = chatCache;
        this.cryptorCache = cryptorCache;
    }

    @Override
    public void receive(Message message) {
        MessageHeader header = message.getHeader();
        MessageBody body = message.getBody();
        switch (header.getMessageType()) {
            case LOGIN_BACK:
                ServerResponse response = (ServerResponse) body.getData();
                log.info("login status code:{},message:{}", response.getCode(), response.getContent());
                if (ResponseEnum.getFromCode(response.getCode()) == ResponseEnum.SUCESS) {
                    iStatus.setLogin(true);
                    log.info("login success");
                } else {
                    iStatus.setLogin(false);
                    log.info("login failed");
                }
                break;
            case KEY_EXCHANGE_TO:
            case KEY_EXCHANGE_BACK:
                ExchangeEncryptKey inputKey = (ExchangeEncryptKey) body.getData();
                String sessionId = inputKey.getSessionId();
                if (chatCache.containsKey(sessionId)) {
                    return;
                }

                Cryptor cryptor = cryptorCache.get(sessionId);
                if (cryptor == null) {
                    cryptor = new Cryptor();
                    cryptor.setPubKey(inputKey.getDiffPubKey());
                    cryptor.initPrivateKey();
                    cryptorCache.put(sessionId, cryptor);
                }
                ExchangeEncryptKey outputKey = mergeCryptKey(inputKey, cryptor);

                if (header.getMessageType() == MessageType.KEY_EXCHANGE_TO) {
                    Map<BigInteger, Set<String>> processMap = new HashMap<>();
                    Set<String> initSet = new HashSet<>();
                    initSet.add(myId);
                    processMap.put(cryptor.getExchangeKey(), initSet);

                    ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
                    exchangeKeySet.setExChangeKeyCache(processMap);
                    outputKey.getExchangeKeySetList().add(exchangeKeySet);
                }
                if (cryptor.getCypherKey() != null) {
                    chatCache.put(sessionId,new HashSet<>(inputKey.getJoinSet()));
                }

                Message newMessage = makeKeyExchangeMsg(outputKey);
                imSender.send(newMessage);
                break;
            case ENCRYPT_TEXT_MSG:
                EncryptText encryptText = (EncryptText) body.getData();
                encryptText.getText();
                break;
            default:
                log.error("un handle message ");
        }
    }

    /**
     * mergge all of the combine key
     *
     * @param inputKey
     * @param cryptor
     * @return
     */
    private ExchangeEncryptKey mergeCryptKey(ExchangeEncryptKey inputKey, Cryptor cryptor) {
        String session = inputKey.getSessionId();

        Set<String> joinSet = inputKey.getJoinSet();
        int joinNumbser = joinSet.size();

        List<ExchangeKeySet> exchangeKeySetList = inputKey.getExchangeKeySetList();

        List<ExchangeKeySet> newExchangeKeySetList = new ArrayList<>();
        exchangeKeySetList.forEach(keySet -> {
            keySet.getExChangeKeyCache().forEach((key, set) -> {
                if (set.size() >= joinNumbser) {
                    return;
                }
                if (!set.contains(myId)) {
                    if (set.size() == joinNumbser - 1) {
                        BigInteger finalKey = cryptor.addExchangeKey(key);
                        cryptor.setCypherKey(finalKey);
                    } else {
                        BigInteger processKey = cryptor.addExchangeKey(key);
                        Map<BigInteger, Set<String>> processMap = new HashMap<>();
                        set.add(myId);
                        processMap.put(processKey, set);
                        ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
                        exchangeKeySet.setExChangeKeyCache(processMap);
                        newExchangeKeySetList.add(exchangeKeySet);
                    }
                }
            });
        });
        ExchangeEncryptKey outputKey = new ExchangeEncryptKey();

        outputKey.setSessionId(session);
        outputKey.setExchangeKeySetList(newExchangeKeySetList);
        outputKey.setDiffPubKey(inputKey.getDiffPubKey());
        outputKey.setJoinSet(joinSet);

        return outputKey;
    }

    /**
     * make up the output message
     *
     * @param outputKey
     * @return
     */
    private Message makeKeyExchangeMsg(ExchangeEncryptKey outputKey) {
        Message newMessage = new Message();
        MessageHeader newHeader = new MessageHeader();
        newHeader.setFrom(myId);
        newHeader.setMessageType(MessageType.KEY_EXCHANGE_BACK);
        newHeader.setVersion(ClientConfig.version);

        Set<String> toSet = new HashSet<>(outputKey.getJoinSet());
        toSet.remove(myId);
        newHeader.setToList(new ArrayList<>(toSet));
        newMessage.setHeader(newHeader);

        MessageBody newBody = new MessageBody();
        newBody.setData(outputKey, ExchangeEncryptKey.class);
        newMessage.setBody(newBody);

        return newMessage;
    }

}
