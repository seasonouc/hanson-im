package com.hanson.im.common.protocol.body;

import com.hanson.im.common.cryption.DiffPubKey;
import com.hanson.im.common.layer.HimSerializer;
import com.hanson.im.common.protocol.util.WriterUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class ExchangeEncryptKey implements HimSerializer {


    /**
     * the encrypt version
     */
    private int version;

    /**
     * session Id
     */
    private String sessionId;


    /**
     * chat name
     */
    private String name;

    /**
     * the collection of exchange keys
     */
    private List<ExchangeKeySet> exchangeKeySetList;

    /**
     * the public key generate by the chat creator
     */
    private DiffPubKey diffPubKey;


    /**
     * the group set
     */
    private Set<String> joinSet;

    public ExchangeEncryptKey() {

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public DiffPubKey getDiffPubKey() {
        return diffPubKey;
    }

    public void setDiffPubKey(DiffPubKey diffPubKey) {
        this.diffPubKey = diffPubKey;
    }

    public List<ExchangeKeySet> getExchangeKeySetList() {
        return exchangeKeySetList;
    }

    public void setExchangeKeySetList(List<ExchangeKeySet> exchangeKeySetList) {
        this.exchangeKeySetList = exchangeKeySetList;
    }

    public Set<String> getJoinSet() {
        return joinSet;
    }

    public void setJoinSet(Set<String> joinSet) {
        this.joinSet = joinSet;
    }

    @Override
    public void writeTo(ByteBuf byteBuffer) {
        WriterUtil.writeString(sessionId, byteBuffer);
        WriterUtil.writeString(name,byteBuffer);
        WriterUtil.writeListString(new ArrayList<>(joinSet), byteBuffer);

        diffPubKey.writeTo(byteBuffer);
        byteBuffer.writeInt(exchangeKeySetList.size());
        exchangeKeySetList.forEach(key -> key.writeTo(byteBuffer));
    }

    @Override
    public void readFrom(ByteBuf byteBuffer) {
        sessionId = WriterUtil.readString(byteBuffer);
        name = WriterUtil.readString(byteBuffer);
        joinSet = new HashSet<>(WriterUtil.readListString(byteBuffer));
        diffPubKey = new DiffPubKey();
        diffPubKey.readFrom(byteBuffer);
        exchangeKeySetList = new ArrayList<>();
        int keyLength = byteBuffer.readInt();
        for (int i = 0; i < keyLength; i++) {
            ExchangeKeySet exchangeKeySet = new ExchangeKeySet();
            exchangeKeySet.readFrom(byteBuffer);
            exchangeKeySetList.add(exchangeKeySet);
        }
    }
}
