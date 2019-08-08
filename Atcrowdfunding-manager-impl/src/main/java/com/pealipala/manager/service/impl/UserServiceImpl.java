package com.pealipala.manager.service.impl;

import com.pealipala.bean.User;
import com.pealipala.exception.LoginFailException;
import com.pealipala.manager.dao.UserMapper;
import com.pealipala.manager.service.UserService;
import com.pealipala.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return user;
    }

//    public Page queryPage(Integer pageno, Integer pagesize) {
//        Page page=new Page(pageno,pagesize);
//        Integer startIndex = page.getStartIndex();
//        List<User> list=userMapper.queryUserPage(startIndex,pagesize);
//        Integer total=userMapper.queryUserCount();
//        page.setDatas(list);
//        page.setTotalsize(total);
//        return page;
//    }

    public int saveUser(User user) {
        return userMapper.insert(user);
    }

    public Page queryPage(Map paramMap) {
        Integer pageno = (Integer) paramMap.get("pageno");
        Integer pagesize = (Integer) paramMap.get("pagesize");
        Page page=new Page(pageno,pagesize);

        Integer startIndex = page.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<User> list=userMapper.queryUserPage(paramMap);
        Integer total=userMapper.queryUserCount(paramMap);
        page.setDatas(list);
        page.setTotalsize(total);
        return page;
    }


}
