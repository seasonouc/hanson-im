package com.hanson.im.server.service.impl;

import com.hanson.im.server.model.User;
import com.hanson.im.server.service.UserCache;
import com.hanson.im.server.user.UserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
public class LocalUserCache implements UserCache{

    Map<String,UserInfo> userInfoMap = new ConcurrentHashMap<String, UserInfo>();

    @Override
    public List<UserInfo> getOnlineUser() {
        Collection<UserInfo> userSet = userInfoMap.values();

        List<UserInfo> list = new ArrayList<>();
        for(UserInfo user:userSet){
            list.add(user);
        }

        return list;
    }

    @Override
    public void setOnlineUser(UserInfo user) {
        userInfoMap.put(user.getId(),user);
    }

    @Override
    public void setOnline(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getUserId());
        userInfo.setUserName(user.getUserName());
        setOnlineUser(userInfo);
    }

    @Override
    public void setOffLineUser(UserInfo user) {
        userInfoMap.remove(user.getId());
    }

    @Override
    public boolean userIsOnline(String userId) {
        return userInfoMap.containsKey(userId);
    }
}
