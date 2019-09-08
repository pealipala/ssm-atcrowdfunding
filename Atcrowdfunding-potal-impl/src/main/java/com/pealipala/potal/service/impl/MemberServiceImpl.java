package com.pealipala.potal.service.impl;

import com.pealipala.bean.Member;
import com.pealipala.potal.dao.MemberMapper;
import com.pealipala.potal.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    public Member login(Map<String, Object> paramMap) {
        return memberMapper.login(paramMap);
    }

    public void updateAcctType(Member member) {
        memberMapper.updateAcctType(member);
    }

    public void updateBasicInfo(Member loginMember) {
        memberMapper.updateBasicInfo(loginMember);
    }

    public void updateEmail(Member loginMember) {
        memberMapper.updateEmail(loginMember);
    }

    public void updateAuthStatus(Member loginMember) {
        memberMapper.updateAuthStatus(loginMember);
    }

    public Member getMemberById(Integer memberid) {
        return memberMapper.getMemberById(memberid);
    }

    public List<Map<String, Object>> queryCertByMemberid(Integer memberid) {
        return memberMapper.queryCertByMemberid(memberid);
    }

    public int insert(Member member) {
        return memberMapper.insert(member);
    }

    public Member selectLogin(String loginacct) {
        return memberMapper.selectByloginacct(loginacct);
    }

    public Member selectForget(String loginacct, String email) {
        return memberMapper.selectForget(loginacct,email);
    }
}
