package com.hanson.im.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.hanson.im.common.protocol.body.UserInfo;
import com.hanson.im.common.vo.res.ResponseVO;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/23
 * @Description:
 */
public class JSONTest {

    @Test
    public void testSeri(){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(100);
        responseVO.setMsg("success");
        List<UserInfo> list = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setId("123");
        userInfo.setUserName("12312");

        list.add(userInfo);

        responseVO.setData(list);

        String res = JSONObject.toJSONString(responseVO);

        ResponseVO newVo = JSONObject.parseObject(res,ResponseVO.class);

        System.out.println(JSONObject.toJSONString(newVo));
    }
}
