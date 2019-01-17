package com.hanson.im.server.service;

import com.hanson.im.server.model.User;
import com.hanson.im.server.user.UserInfo;

import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
public interface UserCache {
    
    List<UserInfo> getOnlineUser();
    
    void setOnlineUser(UserInfo user);

    void setOnline(User user);
    
    void setOffLineUser(String  userId);

    boolean userIsOnline(String userId);

}
