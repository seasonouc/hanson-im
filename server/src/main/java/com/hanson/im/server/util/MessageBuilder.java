package com.hanson.im.server.util;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.LoginResponse;
import com.hanson.im.common.protocol.body.UserInfo;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class MessageBuilder {

    public static Message buildSystemMessage(int code , UserInfo userInfo){
        Message backMessage = new Message();

        MessageHeader backHeader = new MessageHeader();
        backHeader.setMessageType(MessageType.LOGIN_BACK);
        backHeader.setFrom("server");
        backHeader.setVersion(Message.version);
        backMessage.setHeader(backHeader);

        MessageBody backBody = new MessageBody();
        LoginResponse response = new LoginResponse(code, userInfo);
        backBody.setData(response, LoginResponse.class);
        backMessage.setBody(backBody);

        return backMessage;
    }
}
