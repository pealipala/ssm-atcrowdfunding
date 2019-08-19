package com.pealipala.manager.service.impl;

import com.pealipala.bean.Cert;
import com.pealipala.bean.MemberCert;
import com.pealipala.manager.dao.CertMapper;
import com.pealipala.manager.service.CertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertServiceImpl implements CertService {
    @Autowired
    private CertMapper certMapper;

    public List<Cert> queryAllCert() {
        return certMapper.selectAll();
    }

    public List<Cert> queryCertByAccttype(String accttype) {
        return certMapper.queryCertByAccttype(accttype);
    }

    public void saveMemberCert(List<MemberCert> certimgs) {
        for (MemberCert certimg : certimgs) {
            certMapper.saveMemberCert(certimg);
        }
    }
}
