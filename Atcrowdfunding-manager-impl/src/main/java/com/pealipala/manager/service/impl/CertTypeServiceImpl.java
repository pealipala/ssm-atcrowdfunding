package com.pealipala.manager.service.impl;

import com.pealipala.bean.AccountTypeCert;
import com.pealipala.manager.dao.AccountTypeCertMapper;
import com.pealipala.manager.service.CertTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CertTypeServiceImpl implements CertTypeService {

    @Autowired
    private AccountTypeCertMapper accountTypeCertMapper;

    public List<Map<String, Object>> queryAccountTypeCert() {
        return accountTypeCertMapper.queryAccountTypeCert();
    }

    public int insertAcctTypeCert(AccountTypeCert accountTypeCert) {
        return accountTypeCertMapper.insert(accountTypeCert);
    }

    public int deleteAcctTypeCert(Integer certid, String accttype) {
        return accountTypeCertMapper.deleteAcctTypeCert(certid,accttype);
    }


}
