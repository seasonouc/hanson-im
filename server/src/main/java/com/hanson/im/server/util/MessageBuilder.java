package com.hanson.im.server.util;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.ServerResponse;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class MessageBuilder {

    public static Message buildSystemMessage(int code ,String content){
        Message backMessage = new Message();

        MessageHeader backHeader = new MessageHeader();
        backHeader.setMessageType(MessageType.LOGIN_BACK);
        backHeader.setFrom("server");
        backHeader.setVersion(Message.version);
        backMessage.setHeader(backHeader);

        MessageBody backBody = new MessageBody();
        ServerResponse response = new ServerResponse(code, content);
        backBody.setData(response, ServerResponse.class);
        backMessage.setBody(backBody);

        return backMessage;
    }
}
