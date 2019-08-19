package com.pealipala.manager.dao;


import com.pealipala.bean.AccountTypeCert;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AccountTypeCertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountTypeCert record);

    AccountTypeCert selectByPrimaryKey(Integer id);

    List<AccountTypeCert> selectAll();

    int updateByPrimaryKey(AccountTypeCert record);

    List<Map<String,Object>> queryAccountTypeCert();

    int deleteAcctTypeCert(@Param("certid") Integer certid,@Param("accttype") String accttype);
}