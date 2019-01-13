package com.hanson.im.common.protocol.request;

import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class MessageHeaderTest {

    @Test
    public void testWriteAndRead() throws Exception {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setVersion(1.22f);
        messageHeader.setFrom("hanson");
        messageHeader.setMessageType(MessageType.LOGIN_TO);
        List<String> receiver = new ArrayList<>();
        receiver.add("season");
        messageHeader.setToList(receiver);
        ByteBuf byteBuffer = Unpooled.buffer(1024);
        messageHeader.writeTo(byteBuffer);

        MessageHeader messageHeaderRead = new MessageHeader();
        messageHeaderRead.readFrom(byteBuffer);

        Assert.assertEquals(messageHeaderRead.getVersion(),1.22f);
        Assert.assertEquals(messageHeaderRead.getFrom(),"hanson");
        Assert.assertEquals(messageHeaderRead.getMessageType(),MessageType.LOGIN_TO);
        Assert.assertEquals(messageHeaderRead.getToList(),receiver);

    }
    @Test
    public void testByteBuff(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(2014);
        byteBuffer.putInt(1234);
        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.position());
    }

    @Test
    public void testByteBuf(){
        ByteBuf byteBuf = Unpooled.buffer(1234);
        byteBuf.writeInt(12344);
        System.out.println(byteBuf.writerIndex());
        byteBuf.setIndex(5,6);
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());

        System.out.println(byteBuf.resetWriterIndex().writerIndex());
    }
}