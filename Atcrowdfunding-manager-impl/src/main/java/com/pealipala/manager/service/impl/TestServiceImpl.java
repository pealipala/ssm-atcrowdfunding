package com.pealipala.manager.service.impl;

import com.pealipala.manager.dao.TestDao;
import com.pealipala.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    public void insert() {
        Map map=new HashMap();
        map.put("name","123");
        testDao.insert(map);
    }
}
