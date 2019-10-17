package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzy.Service.SetMealService;
import com.lzy.entity.PageResult;
import com.lzy.mapper.SetMealMapper;
import com.lzy.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = SetMealService.class)
@Transactional
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

    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealMapper.addSetMeal(setmeal);
        addGroupIdsBySetMealId(setmeal.getId(), checkGroupIds);
    }

    @Override
    public void addGroupIdsBySetMealId(Integer id, Integer[] checkGroupIds) {
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            for (Integer checkGroupId : checkGroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkGroupId", checkGroupId);
                map.put("setMealId", id);
                setMealMapper.addGroupIdsBySetMealId(map);
            }
        }
    }

    @Override
    public List<Integer> findGroupIdsBySetMealId(Integer setMealId) {
        return setMealMapper.findGroupIdsBySetMealId(setMealId);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealMapper.deleteSetMealAndCheckGroup(setmeal.getId());
        addGroupIdsBySetMealId(setmeal.getId(), checkGroupIds);
        setMealMapper.updateSetMeal(setmeal);
    }

    @Override
    public void delete(Integer setMealId) {
        setMealMapper.deleteSetMealAndCheckGroup(setMealId);
        setMealMapper.deleteSetMealById(setMealId);
    }
}
