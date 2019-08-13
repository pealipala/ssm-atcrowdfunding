package com.pealipala.manager.service;

import com.pealipala.bean.Permission;

import java.util.List;

public interface PermissionService {
    Permission getRootPermission();

    List<Permission> getChildByPid(Integer pid);

    List<Permission> queryAllPermission();

    int savePermission(Permission permission);

    Permission getPermissionById(Integer id);

    int updatePermission(Permission permission);

    int deletePermissionById(Integer id);
}
