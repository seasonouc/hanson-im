package com.hanson.im.server.controller;

import com.hanson.im.common.vo.req.RegisterUserReqVO;
import com.hanson.im.common.vo.res.RegisterUserResVO;
import com.hanson.im.server.service.UserService;
import com.hanson.im.server.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getOnlineUser",method = RequestMethod.POST)
    public UserInfo getOnlineUser(String user){
        return new UserInfo();
    }

    @RequestMapping(value = "registerUser",method = RequestMethod.POST)
    public RegisterUserResVO registerUser(@RequestBody RegisterUserReqVO registerUserReqVO){
        userService.insertUser(registerUserReqVO);
        return new RegisterUserResVO();
    }

    @RequestMapping(value="/{path}",method = RequestMethod.GET)
    public String test(@PathVariable(name = "path") String path){
        return path;
    }

}
