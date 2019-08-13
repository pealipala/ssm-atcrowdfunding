package com.pealipala.manager.service.impl;

import com.pealipala.bean.Role;
import com.pealipala.manager.dao.RoleMapper;
import com.pealipala.manager.service.RoleService;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public Page pageQuery(Map paramMap) {
        Page rolePage = new Page((Integer)paramMap.get("pageno"),(Integer)paramMap.get("pagesize"));

        paramMap.put("startIndex", rolePage.getStartIndex());

        List<Role> roles = roleMapper.pageQuery(paramMap);

        // 获取数据的总条数
        int count = roleMapper.queryRoleCount(paramMap);
        rolePage.setData(roles);
        rolePage.setTotalsize(count);
        return rolePage;
    }

    public int saveRole(Role role) {
        return roleMapper.insert(role);
    }

    public Role selectRoleById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public int updateRole(Role role) {
        return roleMapper.updateByPrimaryKey(role);
    }

    public int deleteRole(Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    public int batchDeleteRole(Data datas) {
        return roleMapper.batchDeleteRole(datas);
    }
}
