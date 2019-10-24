package com.lzy.controller;

import com.lzy.constant.MessageConstant;
import com.lzy.constant.RedisMessageConstant;
import com.lzy.entity.Result;
import com.lzy.utils.SMSUtils;
import com.lzy.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        try {
            //1. 获取四位验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
            //2. 发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
            //3. 把验证码存入redis
            jedisPool.getResource().setex(
                    telephone + RedisMessageConstant.SENDTYPE_LOGIN,
                    5 * 60, validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
