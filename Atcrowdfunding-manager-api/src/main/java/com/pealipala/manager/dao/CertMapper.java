package com.pealipala.manager.dao;

import com.pealipala.bean.Cert;
import com.pealipala.bean.MemberCert;

import java.util.List;

public interface CertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    Cert selectByPrimaryKey(Integer id);

    List<Cert> selectAll();

    int updateByPrimaryKey(Cert record);

    List<Cert> queryCertByAccttype(String accttype);

    void saveMemberCert(MemberCert certimg);
}