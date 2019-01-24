package com.hanson.im.client.network.http;

import com.alibaba.fastjson.JSONObject;
import com.hanson.im.common.protocol.body.UserInfo;
import com.hanson.im.common.vo.req.RegisterUserReqVO;
import org.apache.http.client.methods.HttpGet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.*;

/**
 * @author hanson
 * @Date 2019/1/22
 * @Description:
 */
public class HttpUitlTest {

    @Test
    public void testGet() throws IOException {
        String res = HttpUitl.getHttUtil().get("/v1/getOnlineUser");
        System.out.println(res);
    }

    @Test
    public void testPost() throws IOException {
        Random random = new Random(100);
        RegisterUserReqVO registerUserReqVO = new RegisterUserReqVO();
        registerUserReqVO.setUserId(System.currentTimeMillis()+"");
        registerUserReqVO.setUserName("hanson");
        registerUserReqVO.setPassword("hanson");

        String res = HttpUitl.getHttUtil().post(JSONObject.toJSONString(registerUserReqVO),"/v1/registerUser");
        System.out.println(res);
    }

    @Test
    public void testGetOnlineUser(){
        List<UserInfo> list = HttpUitl.getHttUtil().getOnlineUserList();
        Assert.assertNotNull(list);
    }

}