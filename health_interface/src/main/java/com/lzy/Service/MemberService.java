package com.lzy.Service;

import com.lzy.pojo.Member;

import java.util.List;

public interface MemberService {

    Member findMemberByTel(String telephone);

    void register(Member saveMember);

    Long getMemberByDate(String date);
}
