package com.lzy.mapper;

import com.github.pagehelper.Page;
import com.lzy.pojo.CheckItem;

import java.util.List;

public interface CheckItemMapper {

    Page<CheckItem> selectByCondition(String queryString);

    void save(CheckItem checkItem);

    Long findCountByCheckItemId(Integer id);

    void delete(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findByCheckGroupId(Integer checkGroupId);


}
