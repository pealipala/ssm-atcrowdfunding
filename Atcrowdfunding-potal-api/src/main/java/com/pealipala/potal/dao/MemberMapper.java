package com.pealipala.potal.dao;

import com.pealipala.bean.Member;

import java.util.List;
import java.util.Map;

public interface MemberMapper {
    Member login(Map<String, Object> paramMap);

    void updateAcctType(Member member);

    void updateBasicInfo(Member loginMember);

    void updateEmail(Member loginMember);

    void updateAuthStatus(Member loginMember);

    Member getMemberById(Integer memberid);

    List<Map<String,Object>> queryCertByMemberid(Integer memberid);
}
