package com.lzy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.lzy.Service.OrderService;
import com.lzy.constant.MessageConstant;
import com.lzy.constant.RedisMessageConstant;
import com.lzy.entity.Result;
import com.lzy.pojo.Order;
import com.lzy.utils.DateUtils;
import com.lzy.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(HttpServletResponse response, @RequestBody Map<String, Object> orderMap) {

        //1. 校验验证码
        //1.1 获取手机号码
        String telephone = (String) orderMap.get("telephone");
        //1.2 获取redis中的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //1.3 获取用户传来的验证码
        String validateCode = (String) orderMap.get("validateCode");
        //1.4 判断手机号码和验证码是否为空
        if (telephone == null || validateCode == null) {
            return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }
        //1.5 校验验证码失败
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        Result result = null;
        try {
            //2. 提交预约信息
            //2.1 添加预约类型
            orderMap.put("orderType", Order.ORDERTYPE_WEIXIN);
            //2.2 提交预约, 返回不同的预约信息
            result = orderService.order(orderMap);
        } catch (Exception e) {
            e.printStackTrace();
            //2.3 返回预约异常信息
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3. 发送预约成功短信
        try {
            SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone,(String) orderMap.get("orderDate"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //5 返回预约成功或失败信息
        return result;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            HashMap<String, Object> map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
