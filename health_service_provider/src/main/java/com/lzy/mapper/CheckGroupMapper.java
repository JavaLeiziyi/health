package com.lzy.mapper;

import com.github.pagehelper.Page;
import com.lzy.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupMapper {

    void addCheckGroup(CheckGroup checkGroup);

    void addCheckGroupAndItem(Map map);

    Page<CheckGroup> findPage(String queryString);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkGroupId);

    void updateGroup(CheckGroup checkGroup);

    void deleteItemIdByGroupId(Integer checkGroupId);

    void deleteCheckGroup(Integer groupId);

    List<CheckGroup> findAll();
}
