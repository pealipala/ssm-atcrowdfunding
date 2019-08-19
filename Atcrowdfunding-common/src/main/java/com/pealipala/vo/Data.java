package com.pealipala.vo;

import com.pealipala.bean.MemberCert;
import com.pealipala.bean.User;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class Data {
    private List<User> datas=new ArrayList<User>();
    private List<Integer> ids;
    private List<MemberCert> certimgs = new ArrayList<MemberCert>();
}
