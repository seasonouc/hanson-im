package com.hanson.im.common.vo.req;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */

public class RegisterUserReqVO {

    @NotNull(message = "user id can not be null")
    private String userId;

    @NotNull(message = "user name can not be null")
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
