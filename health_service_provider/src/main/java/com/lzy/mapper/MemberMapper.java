package com.lzy.mapper;

import com.lzy.pojo.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemberMapper {

    Member findMemberByTel(String telephone);

    void register(Member saveMember);

    Long findMemberNumberByDate(Map map);

    Long findTodayMemberCountByDate(String reportDate);

    Long findAllCount();

    List<Map<String, String>> findSex();

    Integer getMemberAgeInfo(HashMap<String, String> tempMap);
}
