package com.hanson.im.common.protocol.util;

import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/15
 * @Description:
 */
public class WriterUtil {

    public static void writeString(String string, ByteBuf byteBuf) {
        byte[] bytes = string.getBytes();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public static String readString(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);
        return new String(bytes);
    }

    public static void writeListString(List<String> list, ByteBuf byteBuf) {
        byteBuf.writeInt(list.size());
        list.forEach(str -> writeString(str, byteBuf));
    }

    public static List<String> readListString(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            String str = readString(byteBuf);
            list.add(str);
        }
        return list;
    }

    public static <T extends HimSerializer> void writeListObject(List<T> list, ByteBuf byteBuf) throws EncodeException {
        byteBuf.writeInt(list.size());
        for(T t:list){
            t.writeTo(byteBuf);
        }
    }
}
