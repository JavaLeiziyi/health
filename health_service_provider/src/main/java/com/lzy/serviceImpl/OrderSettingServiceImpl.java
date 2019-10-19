package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lzy.Service.OrderSettingService;
import com.lzy.mapper.OrderSettingMapper;
import com.lzy.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Override
    public void add(ArrayList<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                //检查此数据(日期)是否存在
                Long count = findCountByOrderDate(orderSetting.getOrderDate());
                addOrEditNumberByDate(count,orderSetting);
            }
        }
    }

    @Override
    public List<Map> getOrderByMonth(String date) {//2019-10
        List<OrderSetting> orderSettingList = exportExcel(date);

        ArrayList<Map> data = null;

        if (orderSettingList != null && orderSettingList.size() > 0) {
            data = new ArrayList<>();
            for (OrderSetting orderSetting : orderSettingList) {
                Map orderSettingMap = new HashMap();
                orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
                orderSettingMap.put("number", orderSetting.getNumber());
                orderSettingMap.put("reservations", orderSetting.getReservations());
                data.add(orderSettingMap);
            }
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //检查此数据(日期)是否存在
        Long count = findCountByOrderDate(orderSetting.getOrderDate());
        addOrEditNumberByDate(count,orderSetting);
    }

    //添加或者更新数据(可预约人数)
    @Override
    public void addOrEditNumberByDate(Long count, OrderSetting orderSetting){
        if (count > 0) {//存在
            //执行更新方法
            orderSettingMapper.updateNumberByOrderDate(orderSetting);
        } else {//不存在
            //执行添加方法
            orderSettingMapper.addOrder(orderSetting);
        }
    }

    //检查此数据(日期)是否存在
    @Override
    public Long findCountByOrderDate(Date date) {
        return orderSettingMapper.findCountByOrderDate(date);
    }

    @Override
    public List<OrderSetting> exportExcel(String orderDate) {
        String dateBegin = orderDate + "-01";
        String dateEnd = orderDate + "-31";
        HashMap<String, String> map = new HashMap<>();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        return orderSettingMapper.getOrderByMonth(map);
    }
}
