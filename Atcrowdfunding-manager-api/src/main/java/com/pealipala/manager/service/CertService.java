package com.pealipala.manager.service;

import com.pealipala.bean.Cert;
import com.pealipala.bean.MemberCert;

import java.util.List;

public interface CertService {
    List<Cert> queryAllCert();

    List<Cert> queryCertByAccttype(String accttype);

    void saveMemberCert(List<MemberCert> certimgs);
}
