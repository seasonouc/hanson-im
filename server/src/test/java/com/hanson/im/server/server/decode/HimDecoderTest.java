package com.hanson.im.server.server.decode;

import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.encode.HimEncoder;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class HimDecoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {

        HimEncoder himEncoder = new HimEncoder();

        MessageHeader header = new MessageHeader();
        header.setFrom("hanson");
        header.setMessageType(MessageType.LOGIN_TO);
        List<String>  list = new ArrayList<>();
        list.add("season");
        header.setToList(list);
        header.setVersion(1.12f);
        MessageBody body = new MessageBody();

        Message message = new Message();

        message.setHeader(header);
        message.setBody(body);

    }
}