package com.pealipala.manager.service;

import com.pealipala.bean.Permission;

import java.util.List;

public interface PermissionService {
    Permission getRootPermission();

    List<Permission> getChildByPid(Integer pid);

    List<Permission> queryAllPermission();
}
