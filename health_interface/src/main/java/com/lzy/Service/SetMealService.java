package com.lzy.Service;

import com.lzy.entity.PageResult;

public interface SetMealService {

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}
