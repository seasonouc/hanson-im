package com.hanson.im.server.service.impl;

import com.hanson.im.common.vo.req.RegisterUserReqVO;
import com.hanson.im.server.dao.UserMapper;
import com.hanson.im.server.model.User;
import com.hanson.im.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(RegisterUserReqVO registerUserReqVO) {
        User user = new User();
        user.setUserId(registerUserReqVO.getUserId());
        user.setUserName(registerUserReqVO.getUserName());

        userMapper.insertSelective(user);
    }

    @Override
    public User getUserById(String userId) {
        return userMapper.selectByUserId(userId);
    }

    @Override
    public List<User> getOnlineUser(int count) {
        return null;
    }
}
