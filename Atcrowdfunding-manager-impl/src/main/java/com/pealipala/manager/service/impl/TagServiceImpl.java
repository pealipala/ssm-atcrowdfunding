package com.pealipala.manager.service.impl;

import com.pealipala.bean.Tag;
import com.pealipala.manager.dao.TagMapper;
import com.pealipala.manager.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    public List<Tag> queryAllTags() {
        return tagMapper.selectAll();
    }

    public int addNewTag(Tag newTag) {
        return tagMapper.insert(newTag);
    }

    public int deleteTag(Integer id) {
        return tagMapper.deleteByPrimaryKey(id);
    }

    public Tag queryTag(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }

    public int updateTag(Tag tag) {
        return tagMapper.updateByPrimaryKey(tag);
    }


}
