package com.hanson.im.client.network.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hanson.im.client.config.ClientConfig;
import com.hanson.im.common.protocol.body.UserInfo;
import com.hanson.im.common.vo.req.RegisterUserReqVO;
import com.hanson.im.common.vo.res.ResponseVO;
import com.hanson.im.common.vo.res.UserInfoResVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
public class HttpUitl {

    private static HttpUitl httpUitl = null;

    private CloseableHttpClient httpClient = null;

    public static HttpUitl getHttUtil() {
        if (httpUitl == null) {
            synchronized (HttpUitl.class) {
                if (httpUitl == null) {
                    httpUitl = new HttpUitl();
                }
            }
        }
        return httpUitl;
    }

    public HttpUitl() {
        httpClient = HttpClients.createDefault();
    }

    public String post(String str, String path) throws IOException {
        HttpPost request = new HttpPost(ClientConfig.http_url + path);
        StringEntity entity = new StringEntity(str);
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entityRes = response.getEntity();
            return EntityUtils.toString(entityRes);
        }
        return null;
    }

    public String get(String path) throws IOException {
        HttpGet request = new HttpGet(ClientConfig.http_url + path);
        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }

        return null;
    }

    public List<UserInfo> getOnlineUserList(){
        try {
            String res = get("/v1/getOnlineUser");
            if(res == null) return null;
            ResponseVO responseVO = JSONObject.parseObject(res,ResponseVO.class );
            List<UserInfo> list = new ArrayList<>();
            JSONArray array = (JSONArray) responseVO.getData();

            for(int i=0;i<array.size();i++){
                JSONObject obj = array.getJSONObject(i);
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(obj.getString("userName"));
                userInfo.setId(obj.getString("id"));
                list.add(userInfo);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registUser(RegisterUserReqVO registerUserReqVO){
        String registString = JSONObject.toJSONString(registerUserReqVO);
        try {
            String res = post(registString,"/v1//registerUser");
            ResponseVO responseVO = JSONObject.parseObject(res,ResponseVO.class);
            if(responseVO.getCode() == 200){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
