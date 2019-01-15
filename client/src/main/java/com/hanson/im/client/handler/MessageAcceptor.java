package com.hanson.im.client.handler;

import com.hanson.im.client.ClientConfig;
import com.hanson.im.common.cryption.Cryptor;
import com.hanson.im.common.protocol.*;
import com.hanson.im.common.protocol.body.ExchangeEncryptKey;
import com.hanson.im.common.protocol.body.ExchangeKeySet;
import com.hanson.im.common.protocol.body.NormalResponse;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.*;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
public class MessageAcceptor implements IMReceiver{

    private boolean isLogin = false;

    /**
     * my user id
     */
    private String myId;

    /**
     * store the chat user set in one chat
     */
    private Map<String,Set<String>> chatCache;

    /**
     * store the cryptor in one chat
     */
    private Map<String,Cryptor> cryptorCache;

    @Override
    public void receive(Message message){
        MessageHeader header = message.getHeader();
        MessageBody body = message.getBody();
        switch (header.getMessageType()){
            case LOGIN_BACK:
                NormalResponse response = (NormalResponse)body.getData();
                log.info("login status code:{},message:{}",response.getCode(),response.getContent());
                if(ResponseEnum.getFromCode(response.getCode()) == ResponseEnum.SUCESS){
                    isLogin = true;
                    log.info("login success");
                }else{
                    log.info("login failed");
                }
                break;
            case KEY_EXCHANGE_TO:
                ExchangeEncryptKey exchangeEncryptKey = (ExchangeEncryptKey)body.getData();
                String session = exchangeEncryptKey.getSessionId();
                if(chatCache.containsKey(session)){
                    return ;
                }
                Cryptor cryptor = new Cryptor();
                cryptor.setPubKey(exchangeEncryptKey.getDiffPubKey());
                cryptor.initPrivateKey();

                Set<String> joinSet = exchangeEncryptKey.getJoinSet();
                int joinNumbser = joinSet.size();

                if(cryptor.getCypherKey() != null){
                    return;
                }
                List<ExchangeKeySet> exchangeKeySetList = exchangeEncryptKey.getExchangeKeySetList();

                List<ExchangeKeySet> newExchangeKeySetList = new ArrayList<>();
                exchangeKeySetList.forEach(keySet->{
                    keySet.getExChangeKeyCache().forEach((key,set)->{
                        if(set.size() >= joinNumbser){
                            return;
                        }
                        if(!set.contains(myId)){
                            if(set.size() == joinNumbser - 1){
                               BigInteger finalKey =  cryptor.addExchangeKey(key);
                               cryptor.setCypherKey(finalKey);
                            }else{
                                BigInteger processKey =cryptor.addExchangeKey(key);
                                Map<BigInteger,Set<String>> processMap = new HashMap<>();
                                set.add(myId);
                                processMap.put(processKey,set);
                                ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
                                exchangeKeySet.setExChangeKeyCache(processMap);
                                newExchangeKeySetList.add(exchangeKeySet);
                            }
                        }
                    });
                });

                Map<BigInteger,Set<String>> processMap = new HashMap<>();
                Set<String> initSet = new HashSet<>();
                initSet.add(myId);
                processMap.put(cryptor.getExchangeKey(),initSet);

                ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
                exchangeKeySet.setExChangeKeyCache(processMap);
                newExchangeKeySetList.add(exchangeKeySet);

                Message newMessage = new Message();
                MessageHeader newHeader = new MessageHeader();
                newHeader.setFrom(myId);
                newHeader.setMessageType(MessageType.KEY_EXCHANGE_BACK);
                newHeader.setVersion(ClientConfig.version);
                joinSet.remove(myId);
                newHeader.setToList(new ArrayList<>(joinSet));

                break;
            case KEY_EXCHANGE_BACK:
                break;
            case ENCRYPT_TEXT_MSG:
                break;
            default:
                log.error("un handle message ");
        }
    }

}
