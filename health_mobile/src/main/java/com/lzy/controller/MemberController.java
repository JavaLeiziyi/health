package com.lzy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lzy.Service.MemberService;
import com.lzy.constant.MessageConstant;
import com.lzy.constant.RedisMessageConstant;
import com.lzy.entity.Result;
import com.lzy.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.DelegatingFilterProxy;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class MemberController{

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map) {
        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");

            //1. 根据用户手机号在redis找到对应的验证码
            String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            //2. 校验验证码是否正确
            if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
                //3. 验证码错误
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //4. 验证码正确, 判断当前用户是否是会员
            Member member = memberService.findMemberByTel(telephone);
            if (member == null) {
                //5. 不是会员,自动完成会员注册
                Member saveMember = new Member();
                saveMember.setPhoneNumber(telephone);
                saveMember.setRegTime(new Date());
                memberService.register(saveMember);
            }

            Member newMember = memberService.findMemberByTel(telephone);

            //6. 向客户端写入cookie, 跟踪客户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            //6.1 配置同一个tomcat中多个web项目可以获取到同一个cookie
            cookie.setPath("/");
            //6.2 设置cookie的持久化存储,一个月
            cookie.setMaxAge(60 * 60 * 24 * 30);
            //6.3 响应cookie对象
            response.addCookie(cookie);

            //7. 保存会员信息到Redis中,保存30分钟, 值必须为String类型
            //7.1 使用fastJson转换为json格式的字符串
            String memberStr = JSON.toJSON(newMember).toString();
            jedisPool.getResource().setex(telephone, 30 * 60, memberStr);
            //7.2 登录成功
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //7.3 登录失败
            return new Result(false,MessageConstant.LOGIN_FAIL);
        }
    }
}
