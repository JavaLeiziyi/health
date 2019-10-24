package com.lzy.mapper;

import com.github.pagehelper.Page;
import com.lzy.pojo.Setmeal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetMealMapper {
    Page<Setmeal> findCondition(String queryString);

    void addGroupIdsBySetMealId(HashMap<String, Integer> map);

    void addSetMeal(Setmeal setmeal);

    List<Integer> findGroupIdsBySetMealId(Integer setMealId);

    void deleteSetMealAndCheckGroup(Integer id);

    void updateSetMeal(Setmeal setmeal);

    void deleteSetMealById(Integer setMealId);

    Setmeal findSetMealById(Integer id);

    List<Setmeal> findAllSetMeal();

    Setmeal findById(Integer setMealId);

    ArrayList<HashMap<String,Object>> findHotSetMeal();
}
