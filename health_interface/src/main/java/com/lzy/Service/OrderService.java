package com.lzy.Service;

import com.lzy.entity.Result;
import com.lzy.pojo.Member;
import com.lzy.pojo.Order;
import com.lzy.pojo.OrderSetting;

import java.util.HashMap;
import java.util.Map;

public interface OrderService {

    Result order(Map orderMap) throws Exception;

    //保存预约信息
    Result saveOrderInfo(OrderSetting orderSetting, Order order);

    //注册会员
    Member registerMember(Map orderMap) throws Exception;

    HashMap<String,Object> findById(Integer id) throws Exception;

    Long findCountBysetmealId(Integer id);
}
