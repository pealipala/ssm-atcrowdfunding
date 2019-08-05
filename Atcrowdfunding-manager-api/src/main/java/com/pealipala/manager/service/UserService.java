package com.pealipala.manager.service;

import com.pealipala.bean.User;

import java.util.Map;

public interface UserService {
    User login(Map<String, Object> paramMap);
}
