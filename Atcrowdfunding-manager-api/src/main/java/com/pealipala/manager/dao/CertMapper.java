package com.pealipala.manager.dao;

import com.pealipala.bean.Cert;
import com.pealipala.bean.MemberCert;
import com.pealipala.vo.Data;

import java.util.List;
import java.util.Map;

public interface CertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    Cert selectByPrimaryKey(Integer id);

    List<Cert> selectAll();

    int updateByPrimaryKey(Cert record);

    List<Cert> queryCertByAccttype(String accttype);

    void saveMemberCert(MemberCert certimg);

    List<Cert> pageQuery(Map<String, Object> map);

    Integer queryUserCount(Map<String, Object> map);

    int deletes(Data datas);
}