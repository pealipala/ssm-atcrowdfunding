package com.pealipala.manager.service;

import com.pealipala.bean.Role;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;

import java.util.Map;

public interface RoleService {
    Page pageQuery(Map paramMap);

    int saveRole(Role role);

    Role selectRoleById(Integer id);

    int updateRole(Role role);

    int deleteRole(Integer id);

    int batchDeleteRole(Data datas);
}
