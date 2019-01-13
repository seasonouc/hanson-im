package com.hanson.im.common.protocol;

/**
 * @author hanson
 * @Date 2019/1/10
 * @Description:
 */
public enum MessageType {

    REGISTER_TO("register to"),
    REGISTER_BACK("register back"),

    LOGIN_TO("login to"),
    LOGIN_BACK("login back"),

    KEY_EXCHANGE_TO("key exchange to"),
    KEY_EXCHANGE_BACK("key exchange back"),

    PLAIN_TEXT_MSG("plain text message"),

    ENCRYPT_TEXT_MSG("encrypt text message"),

    PIC_MSG("picture message"),

    MAKE_GROUP_TO("make group to"),
    MAKE_GROUP_BACK("make group back");

    String msg_type;

    MessageType(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getMsgType() {
        return msg_type;
    }
}
