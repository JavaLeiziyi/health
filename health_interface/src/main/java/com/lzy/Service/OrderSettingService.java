package com.lzy.Service;

import com.lzy.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void add(ArrayList<OrderSetting> orderSettingList);

    List<Map<String,Object>> getOrderByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);

    //添加/更新数据
    void addOrEditNumberByDate(Long count, OrderSetting orderSetting);

    //检查此数据(日期)是否存在
    Long findCountByOrderDate(Date date);

    List<OrderSetting> exportExcel(String orderDate);

    void cleanOrderSetting(String s);
}
