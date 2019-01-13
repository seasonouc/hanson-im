package com.hanson.im.server.service.impl;

import com.hanson.im.common.vo.req.RegisterUserReqVO;
import com.hanson.im.server.HimServerApplication;
import com.hanson.im.server.config.ServerAppConfig;
import com.hanson.im.server.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest

@MapperScan("com.hanson.im.server.dao")
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Test
    @Rollback
    public void testInsert(){
        RegisterUserReqVO registerUserReqVO = new RegisterUserReqVO();
        registerUserReqVO.setUserId("321321321321321");
        registerUserReqVO.setUserName("hanson");

        userService.insertUser(registerUserReqVO);
    }
}