package com.pealipala.potal.service.impl;

import com.pealipala.bean.Member;
import com.pealipala.potal.dao.MemberMapper;
import com.pealipala.potal.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    public Member login(Map<String, Object> paramMap) {
        return memberMapper.login(paramMap);
    }
}
