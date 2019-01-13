package com.hanson.im.common.protocol;

import com.hanson.im.common.exception.DecodeException;
import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
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

        byte[] fromByte = from.getBytes();
        byteBuffer.writeInt(fromByte.length);
        byteBuffer.writeBytes(fromByte);

        if (toList == null || toList.size() == 0) {
            byteBuffer.writeInt(0);
            return;
        }

        byteBuffer.writeInt(toList.size());
        for (String str : toList) {
            if (str == null || str.length() == 0) {
                throw new EncodeException("some message receiver is null");
            }

            byte[] bytes = str.getBytes();
            byteBuffer.writeInt(bytes.length);
            byteBuffer.writeBytes(bytes);


        }


    }

    @Override
    public void readFrom(ByteBuf byteBuffer) throws DecodeException {
        version = byteBuffer.readFloat();
        int messageTypeOrder = byteBuffer.readInt();
        if (messageTypeOrder >= MessageType.values().length) {
            throw new DecodeException("the message type if out of range!");
        }
        messageType = MessageType.values()[messageTypeOrder];

        int fromLength = byteBuffer.readInt();
        byte[] fromByte = new byte[fromLength];
        byteBuffer.readBytes(fromByte, 0, fromLength);

        from = new String(fromByte);

        int toListLength = byteBuffer.readInt();
        if (toListLength < 0 || toListLength >= 20) {
            throw new DecodeException("the receiver list is illegal");
        }
        toList = new ArrayList<>(toListLength);

        for (int i = 0; i < toListLength; i++) {
            int toLength = byteBuffer.readInt();
            byte[] toByte = new byte[toLength];
            byteBuffer.readBytes(toByte, 0, toLength);
            String to = new String(toByte);
            toList.add(to);

        }
    }
}
