package com.hanson.im.server.service.impl;

import com.hanson.im.common.protocol.body.LoginRequest;
import com.hanson.im.server.model.User;
import com.hanson.im.server.service.UserService;
import com.hanson.im.server.service.UserVailidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Service
public class UserVailidatorImpl implements UserVailidator{

    @Autowired
    private UserService userService;

    @Override
    public boolean validateLogin(LoginRequest loginRequest) {

        User user = userService.getUserById(loginRequest.getUserId());
        if(user != null){
            return true;
        }
        return false;
    }
}
