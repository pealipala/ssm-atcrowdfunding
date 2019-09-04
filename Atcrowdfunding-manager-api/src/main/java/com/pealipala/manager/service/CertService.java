package com.pealipala.manager.service;

import com.pealipala.bean.Cert;
import com.pealipala.bean.MemberCert;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;

import java.util.List;
import java.util.Map;

public interface CertService {
    List<Cert> queryAllCert();

    List<Cert> queryCertByAccttype(String accttype);

    void saveMemberCert(List<MemberCert> certimgs);

    Page pageQuery(Map<String, Object> map);

    int insert(Cert cert);

    int delete(Cert cert);

    int deletes(Data datas);

    int update(Cert cert);

    Cert queryById(Integer id);
}

