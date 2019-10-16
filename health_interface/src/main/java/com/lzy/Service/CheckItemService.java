package com.lzy.Service;

import com.lzy.entity.PageResult;
import com.lzy.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void add(CheckItem checkItem);

    Long findCountByCheckItemId(Integer id);

    void delete(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
