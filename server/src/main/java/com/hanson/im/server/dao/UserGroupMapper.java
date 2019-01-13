package com.hanson.im.server.dao;

import com.hanson.im.server.model.UserGroup;

public interface UserGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserGroup record);

    int insertSelective(UserGroup record);

    UserGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserGroup record);

    int updateByPrimaryKeyWithBLOBs(UserGroup record);

    int updateByPrimaryKey(UserGroup record);
}