package com.hanson.im.common.protocol.util;

import com.hanson.im.common.exception.EncodeException;
import com.hanson.im.common.layer.HimSerializer;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (list == null) {
            byteBuf.writeInt(0);
            return;
        }
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

    public static void writeSetString(Set<String> set, ByteBuf byteBuf) {
        if (set == null) {
            byteBuf.writeInt(0);
            return;
        }
        byteBuf.writeInt(set.size());
        set.forEach(str -> writeString(str, byteBuf));
    }

    public static Set<String> readSetString(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < length; i++) {
            String str = readString(byteBuf);
            set.add(str);
        }
        return set;
    }

    public static void writeBigInt(BigInteger b, ByteBuf byteBuf) {
        byte[] bytesP = b.toByteArray();
        byteBuf.writeInt(bytesP.length);
        byteBuf.writeBytes(bytesP);
    }

    public static BigInteger readBigInt(ByteBuf byteBuf) {
        int pLength = byteBuf.readInt();
        byte[] pBytes = new byte[pLength];
        byteBuf.readBytes(pBytes, 0, pLength);
        return new BigInteger(pBytes);
    }

    public static <T extends HimSerializer> void writeListObject(List<T> list, ByteBuf byteBuf) throws EncodeException {
        byteBuf.writeInt(list.size());
        for (T t : list) {
            t.writeTo(byteBuf);
        }
    }
}
