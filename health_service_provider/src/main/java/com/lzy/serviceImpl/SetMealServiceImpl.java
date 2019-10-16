package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzy.Service.SetMealService;
import com.lzy.entity.PageResult;
import com.lzy.mapper.SetMealMapper;
import com.lzy.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        if (queryString != null && queryString.length() > 0) {
            queryString = "%" + queryString + "%";
        }
        Page<Setmeal> setMealPage = setMealMapper.findCondition(queryString);
        return new PageResult(setMealPage.getTotal(), setMealPage.getResult());
    }
}
