package com.hanson.im.common.protocol;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.protocol.body.LoginResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author hanson
 * @Date 2019/1/13
 * @Description:
 */
public class MessageBodyTest {

//    @Test
//    public void testWriteAndRead() throws ClassNotFoundException {
//        MessageBody body = new MessageBody();
//        LoginResponse response = new LoginResponse(404,"login success!");
//        body.setData(response,LoginResponse.class);
//        ByteBuf byteBuf = Unpooled.buffer(2048);
//
//        try {
//            body.writeTo(byteBuf);
//        } catch (EncodeException e) {
//            e.printStackTrace();
//        }
//
//        MessageBody bodyRead = new MessageBody();
//
//        try {
//            bodyRead.readFrom(byteBuf);
//        } catch (DecodeException e) {
//            e.printStackTrace();
//        }
//        Assert.assertEquals(((LoginResponse)body.getData()).getCode(),((LoginResponse)bodyRead.getData()).getCode());
//        Assert.assertEquals(((LoginResponse)body.getData()).getContent(),((LoginResponse)bodyRead.getData()).getContent());
//    }

}