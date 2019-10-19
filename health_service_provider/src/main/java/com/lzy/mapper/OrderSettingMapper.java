package com.lzy.mapper;

import com.lzy.pojo.OrderSetting;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OrderSettingMapper {
    Long findCountByOrderDate(Date orderDate);

    void updateNumberByOrderDate(OrderSetting orderSetting);

    void addOrder(OrderSetting orderSetting);

    List<OrderSetting> getOrderByMonth(HashMap<String, String> map);
}
