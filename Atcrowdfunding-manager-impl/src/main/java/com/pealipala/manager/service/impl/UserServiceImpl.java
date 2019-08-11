package com.pealipala.manager.service.impl;

import com.pealipala.bean.Role;
import com.pealipala.bean.User;
import com.pealipala.exception.LoginFailException;
import com.pealipala.manager.dao.UserMapper;
import com.pealipala.manager.service.UserService;
import com.pealipala.utils.Const;
import com.pealipala.utils.MD5Util;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String createtime = simpleDateFormat.format(date);
        user.setCreatetime(createtime);
        user.setUserpswd(MD5Util.digest(Const.LOGIN_PASSWORD));
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

    public User queryUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public int upDateUser(User user) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String createtime = simpleDateFormat.format(date);
        user.setCreatetime(createtime);
        User select = userMapper.selectByPrimaryKey(user.getId());
        user.setUserpswd(select.getUserpswd());
        return userMapper.updateByPrimaryKey(user);
    }

    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    public int deleteUserBatch(Integer[] ids) {
        int deleteCount=0;
        for (Integer id:ids) {
            int count = userMapper.deleteByPrimaryKey(id);
            deleteCount+=count;
        }
        if (deleteCount!=ids.length){
            throw new RuntimeException("批量删除失败");
        }
        return deleteCount;
    }

    public int deleteUserBatchByVO(Data datas) {
        return userMapper.deleteUserBatchByVO(datas);
    }

    public List<Role> queryAllRole() {
        return userMapper.queryAllRole();
    }

    public List<Integer> queryRoleByUserId(Integer id) {
        return userMapper.queryRoleByUserId(id);
    }

    public int saveUserRoleRelationship(Integer userid, Data data) {
        return userMapper.saveUserRoleRelationship(userid,data);
    }

    public int deleteUserRoleRelationship(Integer userid, Data data) {
        return userMapper.deleteUserRoleRelationship(userid,data);
    }


}
