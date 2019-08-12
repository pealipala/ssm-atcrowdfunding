package com.pealipala.manager.dao;

import com.pealipala.bean.Permission;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> queryAllPermission();

    int updateByPrimaryKey(Permission record);

    Permission getRootPermission();

    List<Permission> getChildByPid(Integer pid);

}