package com.hanson.im.server.controller;

import com.hanson.im.common.vo.req.RegisterUserReqVO;
import com.hanson.im.common.vo.res.ResponseVO;
import com.hanson.im.server.service.UserCache;
import com.hanson.im.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @RequestMapping(value = "/getOnlineUser", method = RequestMethod.GET)
    public ResponseVO getOnlineUser() {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(200);
        responseVO.setMsg("success");
        responseVO.setData(userCache.getOnlineUser());

        return responseVO;
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ResponseVO registerUser(@RequestBody @Valid RegisterUserReqVO registerUserReqVO) {
        ResponseVO responseVO = new ResponseVO<>();
        try {
            userService.insertUser(registerUserReqVO);
            responseVO.setMsg("success");
            responseVO.setData(registerUserReqVO);
            responseVO.setCode(200);
        } catch (Exception e) {
            responseVO.setCode(404);
            responseVO.setMsg("failed");
        }
        return responseVO;
    }

    @RequestMapping(value = "/{path}", method = RequestMethod.GET)
    public String test(@PathVariable(name = "path") String path) {
        return path;
    }

}
