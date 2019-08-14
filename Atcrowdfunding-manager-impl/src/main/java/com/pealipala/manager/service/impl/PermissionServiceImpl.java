package com.pealipala.manager.service.impl;

import com.pealipala.bean.Permission;
import com.pealipala.manager.dao.PermissionMapper;
import com.pealipala.manager.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionMapper permissionMapper;

    public Permission getRootPermission() {
        return permissionMapper.getRootPermission();
    }

    public List<Permission> getChildByPid(Integer pid) {
        return permissionMapper.getChildByPid(pid);
    }

    public List<Permission> queryAllPermission() {
        return permissionMapper.queryAllPermission();
    }

    public int savePermission(Permission permission) {
        return permissionMapper.insert(permission);
    }

    public Permission getPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    public int updatePermission(Permission permission) {
        return permissionMapper.updateByPrimaryKey(permission);
    }

    public int deletePermissionById(Integer id) {
        return permissionMapper.deleteByPrimaryKey(id);
    }

    public List<Integer> queryPermissionidsByRoleid(Integer id) {
        return permissionMapper.queryPermissionidsByRoleid(id);
    }
}
