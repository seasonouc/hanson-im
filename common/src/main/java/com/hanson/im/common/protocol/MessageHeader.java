package com.hanson.im.common.protocol;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import com.hanson.im.common.protocol.util.WriterUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/10
 * @Description:
 */
public class MessageHeader implements HimSerializer {

    /**
     * the protocol version
     */
    private float version;
    /**
     * the message sender
     */
    private String from;
    /**
     * the message receiver list
     */
    private List<String> toList;
    /**
     * the message type
     */
    private MessageType messageType;

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }


    @Override
    public void writeTo(ByteBuf byteBuffer) throws EncodeException {
        byteBuffer.writeFloat(version);
        byteBuffer.writeInt(messageType.ordinal());

        if (from == null || from.length() == 0) {
            throw new EncodeException("message sender is null");
        }

        WriterUtil.writeString(from,byteBuffer);
        WriterUtil.writeListString(toList,byteBuffer);
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        version = byteBuffer.readFloat();
        int messageTypeOrder = byteBuffer.readInt();
        if (messageTypeOrder >= MessageType.values().length) {
            throw new DecodeException("the message type if out of range!");
        }
        messageType = MessageType.values()[messageTypeOrder];

        from = WriterUtil.readString(byteBuffer);
        toList = WriterUtil.readListString(byteBuffer);

    }
}
