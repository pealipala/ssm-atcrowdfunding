package com.pealipala.manager.service;

import com.pealipala.bean.AccountTypeCert;

import java.util.List;
import java.util.Map;

public interface CertTypeService {
    List<Map<String,Object>> queryAccountTypeCert();

    int insertAcctTypeCert(AccountTypeCert accountTypeCert);

    int deleteAcctTypeCert(Integer certid, String accttype);
}
