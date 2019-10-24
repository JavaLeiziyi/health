package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lzy.Service.OrderService;
import com.lzy.constant.MessageConstant;
import com.lzy.entity.Result;
import com.lzy.mapper.MemberMapper;
import com.lzy.mapper.OrderMapper;
import com.lzy.mapper.OrderSettingMapper;
import com.lzy.pojo.Member;
import com.lzy.pojo.Order;
import com.lzy.pojo.OrderSetting;
import com.lzy.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Autowired
    private MemberMapper memberMapper;

    //提交预约
    @Override
    public Result order(Map orderMap) throws Exception {
        //1. 查询当日是否进行了预约设置
        String orderDate = (String) orderMap.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingMapper.findOrderSetByOrderDate(date);
        if (orderSetting == null) {
            //1.1 没有进行预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2. 查询当日预约人数是否已满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            //2.1 当日预约人数已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3. 检查本号码用户是否为会员
        String telephone = (String) orderMap.get("telephone");
        Member member = memberMapper.findMemberByTel(telephone);
        //3.1 是会员
        if (member != null) {
            //3.2 判断该用户是否已经预约本套餐
            Integer memberId = member.getId();
            Integer setmealId = Integer.parseInt((String) orderMap.get("setmealId"));
            //3.3 根据会员ID和套餐ID以及日期来判断
            Order selectOrder = new Order(memberId, date, null, null, setmealId);
            List<Order> orders = orderMapper.findMemberByCondition(selectOrder);
            //3.4 orders存在数据, 此会员当日本套餐已经预约
            if (orders != null && orders.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            } else {
                //3.5 orders不存在数据,可以预约
                //3.6 保存预约实体
                Order saveOrder = new Order(
                        memberId,
                        date,//预约体检时间, 是会员指定
                        (String) orderMap.get("orderType"),
                        Order.ORDERSTATUS_NO,
                        setmealId
                );
                //3.7 预约人数+1,保存预约信息, 返回预约成功的信息
                return saveOrderInfo(orderSetting, saveOrder);
            }
        }

        Member saveMember = null;
        //4. 不是会员, 注册会员
        if (member == null) {
            saveMember = registerMember(orderMap);
        }
        //4.1 保存预约实体
        Order saveOrder = new Order(
                saveMember.getId(),
                date,//预约体检时间, 是会员指定
                (String) orderMap.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) orderMap.get("setmealId"))
        );
        //4.2 预约人数+1,保存预约信息, 返回预约成功的信息
        return saveOrderInfo(orderSetting, saveOrder);

    }

    //保存预约信息
    @Override
    public Result saveOrderInfo(OrderSetting orderSetting, Order order) {
        //1. 设置预约人数+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingMapper.updateNumberByOrderDate(orderSetting);
        //2. 保存预约信息
        orderMapper.save(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    //注册会员
    @Override
    public Member registerMember(Map orderMap) throws Exception {

        Member saveMember = new Member();
        saveMember.setName((String) orderMap.get("name"));
        saveMember.setSex((String) orderMap.get("sex"));
        saveMember.setIdCard((String) orderMap.get("idCard"));
        saveMember.setPhoneNumber((String) orderMap.get("telephone"));
        saveMember.setRegTime(new Date());//注册时间是固定当前时间

        memberMapper.register(saveMember);
        return saveMember;
    }

    @Override
    public HashMap<String,Object> findById(Integer id) throws Exception {
        HashMap<String,Object> map = orderMapper.findById(id);
        if (map != null && map.size() > 0) {
            //转换日期格式
            Date orderDate = (Date) map.get("orderDate");
            String dateStr = DateUtils.parseDate2String(orderDate);
            map.put("orderDate", dateStr);
        }
        return map;
    }

    @Override
    public Long findCountBysetmealId(Integer id) {
        return orderMapper.findCountBysetmealId(id);
    }
}
