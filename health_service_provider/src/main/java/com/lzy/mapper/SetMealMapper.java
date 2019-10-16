package com.lzy.mapper;

import com.github.pagehelper.Page;
import com.lzy.pojo.Setmeal;

public interface SetMealMapper {
    Page<Setmeal> findCondition(String queryString);
}
