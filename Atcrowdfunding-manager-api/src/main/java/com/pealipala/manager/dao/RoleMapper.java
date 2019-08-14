package com.pealipala.manager.dao;

import com.pealipala.bean.Role;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Role selectByPrimaryKey(Integer id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    List<Role> pageQuery(Map paramMap);

    Integer queryRoleCount(Map paramMap);

    int batchDeleteRole(Data datas);

    void deleteByRoleid(Integer roleid);
}