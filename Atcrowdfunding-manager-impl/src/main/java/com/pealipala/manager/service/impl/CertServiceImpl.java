package com.pealipala.manager.service.impl;

import com.pealipala.bean.Cert;
import com.pealipala.bean.MemberCert;
import com.pealipala.manager.dao.CertMapper;
import com.pealipala.manager.service.CertService;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public Page pageQuery(Map<String, Object> map) {
        Integer pageno = (Integer) map.get("pageno");
        Integer pagesize = (Integer) map.get("pagesize");
        Page page=new Page(pageno,pagesize);
        map.put("startIndex",page.getStartIndex());
        List<Cert> list=certMapper.pageQuery(map);
        Integer total=certMapper.queryUserCount(map);
        page.setData(list);
        page.setTotalsize(total);
        return page;
    }

    public int insert(Cert cert) {
        return certMapper.insert(cert);
    }

    public int delete(Cert cert) {
        return certMapper.deleteByPrimaryKey(cert.getId());
    }

    public int deletes(Data datas) {
        return certMapper.deletes(datas);
    }

    public int update(Cert cert) {
        return certMapper.updateByPrimaryKey(cert);
    }

    public Cert queryById(Integer id) {
        return certMapper.selectByPrimaryKey(id);
    }
}
