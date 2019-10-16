package com.lzy.Service;

import com.lzy.entity.PageResult;
import com.lzy.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    void addCheckGroupAndItem(Integer checkgroupId, Integer[] checkitemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkGroupId);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void delete(Integer groupId);

    List<CheckGroup> findAll();
}