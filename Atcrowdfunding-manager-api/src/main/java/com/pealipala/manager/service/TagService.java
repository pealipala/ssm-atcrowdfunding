package com.pealipala.manager.service;

import com.pealipala.bean.Tag;

import java.util.List;

public interface TagService {
    List<Tag> queryAllTags();

    int addNewTag(Tag newTag);

    int deleteTag(Integer id);

    Tag queryTag(Integer id);

    int updateTag(Tag tag);
}
