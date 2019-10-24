package com.lzy.mapper;

import com.lzy.pojo.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderMapper {

    List<Order> findMemberByCondition(Order selectOrder);

    void save(Order order);

    HashMap<String,Object> findById(Integer id);

    Long findCountBysetmealId(Integer id);

    Long findCountByDate(Map reportDate);

    Long findCountInDate(Map dateWeekMap);
}
