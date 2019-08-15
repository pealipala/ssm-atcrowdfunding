package com.pealipala.manager.service;

import com.pealipala.bean.Advertisement;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;

import java.util.Map;

public interface AdvertService {
    Page queryAllAdv(Map<String, Object> paramMap);

    int insertAdvert(Advertisement advertisement);

    Advertisement queryById(Integer id);

    int deleteByid(Integer id);

    int batchDeleteByid(Data datas);
}
