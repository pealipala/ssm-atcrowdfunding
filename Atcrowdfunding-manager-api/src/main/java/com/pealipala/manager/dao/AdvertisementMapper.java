package com.pealipala.manager.dao;

import com.pealipala.bean.Advertisement;
import com.pealipala.vo.Data;

import java.util.List;
import java.util.Map;

public interface AdvertisementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advertisement record);

    Advertisement selectByPrimaryKey(Integer id);

    List<Advertisement> selectAll();

    int updateByPrimaryKey(Advertisement record);

    List<Advertisement> queryAllAdv(Map<String, Object> paramMap);

    int queryCount();

    int batchDeleteByid(Data datas);
}