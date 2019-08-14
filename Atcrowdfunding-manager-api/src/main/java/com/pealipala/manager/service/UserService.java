package com.pealipala.manager.service;

import com.pealipala.bean.Permission;
import com.pealipala.bean.Role;
import com.pealipala.bean.User;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;


import java.util.List;
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

    int deleteUserBatchByVO(Data datas);

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserId(Integer id);

    int saveUserRoleRelationship(Integer userid, Data data);

    int deleteUserRoleRelationship(Integer userid, Data data);

    List<Permission> queryPermissionById(Integer id);
}
