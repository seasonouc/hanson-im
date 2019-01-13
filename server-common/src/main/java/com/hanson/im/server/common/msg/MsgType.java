package com.hanson.im.server.common.msg;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
public enum MsgType {

    HEARTBEAT_REQUEST("heartbeat request"),
    HEARTBEAT_RESPONSE("heartbeat response");

    String msgType;
    MsgType(String msgType){
        this.msgType = msgType;
    }

    public String getMsgType(){
        return msgType;
    }
}
