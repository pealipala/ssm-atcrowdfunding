package com.pealipala.manager.service.impl;

import com.pealipala.bean.Advertisement;
import com.pealipala.manager.dao.AdvertisementMapper;
import com.pealipala.manager.service.AdvertService;
import com.pealipala.utils.Page;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdvertServiceImpl implements AdvertService{
    @Autowired
    private AdvertisementMapper advertisementMapper;

    public Page queryAllAdv(Map<String, Object> paramMap) {
        Integer pageno = (Integer) paramMap.get("pageno");
        Integer pagesize = (Integer) paramMap.get("pagesize");
        Page page=new Page(pageno,pagesize);
        Integer startIndex = page.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Advertisement> list = advertisementMapper.queryAllAdv(paramMap);
        int count = advertisementMapper.queryCount();
        page.setTotalsize(count);
        page.setData(list);
        return page;
    }

    public int insertAdvert(Advertisement advertisement) {
        return advertisementMapper.insert(advertisement);
    }

    public Advertisement queryById(Integer id) {
        return advertisementMapper.selectByPrimaryKey(id);
    }

    public int deleteByid(Integer id) {
        return advertisementMapper.deleteByPrimaryKey(id);
    }

    public int batchDeleteByid(Data datas) {
        return advertisementMapper.batchDeleteByid(datas);
    }
}
