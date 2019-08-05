package com.pealipala.manager.service.impl;

import com.pealipala.bean.User;
import com.pealipala.exception.LoginFailException;
import com.pealipala.manager.dao.UserMapper;
import com.pealipala.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public User login(Map<String, Object> paramMap) {
        User user = userMapper.queryUserLogin(paramMap);
        if (user==null){
            throw new LoginFailException("用户名或者密码不正确");
        }
        return null;
    }
}
