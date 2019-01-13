package com.hanson.im.server.dao;

import com.hanson.im.server.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User selectByUserId(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


}