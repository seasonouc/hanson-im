package com.hanson.im.server.service;

import com.hanson.im.common.vo.req.RegisterUserReqVO;
import com.hanson.im.server.model.User;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
public interface UserService {

    void insertUser(RegisterUserReqVO registerUserReqVO);

    User getUserById(String userId);
}
