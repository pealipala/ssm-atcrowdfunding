package com.pealipala.manager.dao;

import com.pealipala.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

	User queryUserLogin(Map<String, Object> paramMap);

    List<User> queryUserPage(Map paramMap);

    Integer queryUserCount(Map paramMap);

//    List<User> queryUserPage(@Param("startIndex")Integer startIndex,@Param("pagesize") Integer pagesize);
//
//    Integer queryUserCount();
}