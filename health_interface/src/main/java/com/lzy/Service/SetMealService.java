package com.lzy.Service;

import com.lzy.entity.PageResult;
import com.lzy.pojo.Setmeal;

import java.util.List;

public interface SetMealService {

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    //把图片信息保存到redis
    void addRedis(String fileName);

    void addGroupIdsBySetMealId(Integer id, Integer[] checkgroupIds);

    List<Integer> findGroupIdsBySetMealId(Integer setMealId);

    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    void delete(Integer setMealId);
}
