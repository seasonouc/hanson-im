package com.hanson.im.client.handler;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.body.ExchangeEncryptKey;
import com.hanson.im.common.protocol.body.NormalResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
public class MessageAcceptor {

    public void accept(Message message){
        MessageHeader header = message.getHeader();
        MessageBody body = message.getBody();
        switch (header.getMessageType()){
            case LOGIN_BACK:
                NormalResponse response = (NormalResponse)body.getData();
                log.info("login status code:{},message:{}",response.getCode(),response.getContent());
                break;
            case KEY_EXCHANGE_TO:
                ExchangeEncryptKey exchangeEncryptKey = (ExchangeEncryptKey)body.getData();
                int version = exchangeEncryptKey.getVersion();
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
