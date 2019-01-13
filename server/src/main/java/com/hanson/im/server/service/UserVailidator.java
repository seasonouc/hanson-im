package com.hanson.im.server.service;

import com.hanson.im.common.protocol.body.LoginRequest;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public interface UserVailidator {

    /**
     * if a user can login
     * @param loginRequest
     * @return
     */
    boolean validateLogin(LoginRequest loginRequest);
}
