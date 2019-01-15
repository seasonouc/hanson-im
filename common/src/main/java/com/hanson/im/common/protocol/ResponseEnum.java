package com.hanson.im.common.protocol;

/**
 * @author hanson
 * @Date 2019/1/15
 * @Description:
 */
public enum ResponseEnum {

    /**
     * operation success code
     */
    SUCESS(200,"operation success"),

    /**
     * operation failed code
     */
    FAILED(400,"operation failed"),

    /**
     * system error
     */
    SYS_ERR(500,"System error");

    /**
     * status code
     */
    private int code;

    /**
     * status message
     */
    private String message;

    ResponseEnum(int code,String message){
        this.code = code;
        this.message = message;
    }

    public static ResponseEnum getFromCode(int code){
        for(ResponseEnum response:values()){
            if(response.getCode() == code){
                return response;
            }
        }
        return null;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
}
