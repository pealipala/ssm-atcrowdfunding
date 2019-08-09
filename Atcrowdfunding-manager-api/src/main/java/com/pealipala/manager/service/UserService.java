package com.pealipala.manager.service;

import com.pealipala.bean.User;
import com.pealipala.utils.Page;


import java.util.Map;

public interface UserService {
    User login(Map<String, Object> paramMap);

//    Page queryPage(Integer pageno, Integer pagesize);

    int saveUser(User user);

    Page queryPage(Map paramMap);

    User queryUserById(Integer id);

    int upDateUser(User user);

    int deleteUser(Integer id);

    int deleteUserBatch(Integer[] ids);
}
