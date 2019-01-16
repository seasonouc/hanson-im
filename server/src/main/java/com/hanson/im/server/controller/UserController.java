package com.hanson.im.server.controller;

import com.hanson.im.common.vo.req.RegisterUserReqVO;
import com.hanson.im.common.vo.res.RegisterUserResVO;
import com.hanson.im.common.vo.res.ReponseVO;
import com.hanson.im.server.service.UserCache;
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

    @Autowired
    private UserCache userCache;

    @RequestMapping(value = "/getOnlineUser",method = RequestMethod.GET)
    public ReponseVO getOnlineUser(){
        ReponseVO reponseVO = new ReponseVO();
        reponseVO.setCode(200);
        reponseVO.setMsg("success");
        reponseVO.setData(userCache.getOnlineUser());

        return reponseVO;
    }

    @RequestMapping(value = "registerUser",method = RequestMethod.POST)
    public ReponseVO registerUser(@RequestBody RegisterUserReqVO registerUserReqVO){
        ReponseVO responseVO = new ReponseVO<>();
        try {
            userService.insertUser(registerUserReqVO);
            responseVO.setMsg("success");
            responseVO.setData(registerUserReqVO);
        }catch (Exception e){
            responseVO.setMsg(e.getMessage());
        }
        return responseVO;
    }

    @RequestMapping(value="/{path}",method = RequestMethod.GET)
    public String test(@PathVariable(name = "path") String path){
        return path;
    }

}
