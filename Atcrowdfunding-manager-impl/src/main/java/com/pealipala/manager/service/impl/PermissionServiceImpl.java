package com.pealipala.manager.service.impl;

import com.pealipala.manager.dao.PermissionMapper;
import com.pealipala.manager.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionMapper permissionMapper;
}
