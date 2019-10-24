package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lzy.Service.MemberService;
import com.lzy.mapper.MemberMapper;
import com.lzy.pojo.Member;
import com.lzy.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member findMemberByTel(String telephone) {
        return memberMapper.findMemberByTel(telephone);
    }

    @Override
    public void register(Member saveMember) {
        //获取会员的密码
        if(saveMember.getPassword()!=null){
            //密码加密
            saveMember.setPassword(MD5Utils.md5(saveMember.getPassword()));
        }
        memberMapper.register(saveMember);
    }

    @Override
    public Long getMemberByDate(String date) {
        HashMap<String, Object> map = new HashMap<>();
        //获取当前月的第一天和最后一天
        String dateBegin = date + "-01";
        String dateEnd = date + "-31";
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        //查询当月的会员数量
        Long count = memberMapper.findMemberNumberByDate(map);
        return count;
    }
}
