package com.pealipala.potal.dao;

import com.pealipala.bean.Member;

import java.util.Map;

public interface MemberMapper {
    Member login(Map<String, Object> paramMap);
}
