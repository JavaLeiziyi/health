package com.lzy.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.lzy.Service.SetMealService;
import com.lzy.constant.MessageConstant;
import com.lzy.constant.RedisMessageConstant;
import com.lzy.entity.Result;
import com.lzy.pojo.Setmeal;
import com.lzy.utils.SMSUtils;
import com.lzy.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/getSetmeal")
    public Result getSetMeal() {
        try {
            List<Setmeal> setMeals = setMealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setMeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/sendCheckCode")
    public Result sendCheckCode(String telephone) {
        //随机生成六位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //验证码存入到redis中, 保存5分钟
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,
                5 * 60, validateCode.toString());

        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
