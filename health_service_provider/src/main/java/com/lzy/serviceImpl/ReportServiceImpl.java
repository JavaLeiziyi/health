package com.lzy.serviceImpl;

import java.lang.reflect.Array;
import java.util.*;

import com.alibaba.dubbo.config.annotation.Service;
import com.lzy.Service.ReportService;
import com.lzy.mapper.MemberMapper;
import com.lzy.mapper.OrderMapper;
import com.lzy.mapper.SetMealMapper;
import com.lzy.pojo.Order;
import com.lzy.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public HashMap<String, Object> getReportData() throws Exception {
        HashMap<String, Object> reportData = new HashMap<String, Object>();
        /**
         * reportDate: null,
         * todayNewMember: 0,
         * totalMember: 0,
         * thisWeekNewMember: 0,
         * thisMonthNewMember: 0,
         * todayOrderNumber: 0,
         * todayVisitsNumber: 0,
         * thisWeekOrderNumber: 0,
         * thisWeekVisitsNumber: 0,
         * thisMonthOrderNumber: 0,
         * thisMonthVisitsNumber: 0,
         * hotSetmeal: [
         *     {name: '阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐', setmeal_count: 200, proportion: 0.222},
         *     {name: '阳光爸妈升级肿瘤12项筛查体检套餐', setmeal_count: 200, proportion: 0.222}
         * ]
         */
        //0. 封装日期的集合
        HashMap<String, Object> dateMap = new HashMap<String, Object>();
        //0.1 获取当前时间
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        //0.2 获取本周一的日期
        Date dateBeginWeek = DateUtils.getThisWeekMonday();
        //0.3 获取本周天的日期
        Date dateEndWeek = DateUtils.getSundayOfThisWeek();
        //0.4 获取本月一号的日期
        Date dateBeginMonth = DateUtils.getFirstDay4ThisMonth();
        //0.5 获取本月最后一天的日期
        String format = new SimpleDateFormat("yyyy-MM").format(dateBeginMonth) + "-31";
        Date dateEndMonth = DateUtils.parseString2Date(format);

        //1. 封装当前时间
        reportData.put("reportDate", reportDate);

        //2. 获取当天的会员数
        Long todayNewMember = memberMapper.findTodayMemberCountByDate(reportDate);
        reportData.put("todayNewMember", todayNewMember);

        //3.获取总会员数
        Long totalMember = memberMapper.findAllCount();
        reportData.put("totalMember", totalMember);

        //4. 获取本周的会员数
        dateMap.put("dateBegin", dateBeginWeek);
        dateMap.put("dateEnd", dateEndWeek);
        Long thisWeekNewMember = memberMapper.findMemberNumberByDate(dateMap);
        reportData.put("thisWeekNewMember", thisWeekNewMember);

        //5. 获取本月的会员数
        dateMap.put("dateBegin", dateBeginMonth);
        dateMap.put("dateEnd", dateEndMonth);
        Long thisMonthNewMember = memberMapper.findMemberNumberByDate(dateMap);
        reportData.put("thisMonthNewMember", thisMonthNewMember);

        //6. 查询当天的预约数量
        dateMap.put("orderDate", reportDate);
        Long todayOrderNumber = orderMapper.findCountByDate(dateMap);
        reportData.put("todayOrderNumber", todayOrderNumber);

        //7. 查询本周的预约数量
        dateMap.put("dateBegin", dateBeginWeek);
        dateMap.put("dateEnd", dateEndWeek);
        Long thisWeekOrderNumber = orderMapper.findCountInDate(dateMap);
        reportData.put("thisWeekOrderNumber", thisWeekOrderNumber);

        //8. 查询本月的预约数量
        dateMap.put("dateBegin", dateBeginMonth);
        dateMap.put("dateEnd", dateEndMonth);
        Long thisMonthOrderNumber = orderMapper.findCountInDate(dateMap);
        reportData.put("thisMonthOrderNumber", thisMonthOrderNumber);

        //9. 查询当天到诊数
        dateMap.put("orderStatus", Order.ORDERSTATUS_YES);
        Long todayVisitsNumber = orderMapper.findCountByDate(dateMap);
        reportData.put("todayVisitsNumber", todayVisitsNumber);

        //10. 查询本周到诊数
        dateMap.put("dateBegin", dateBeginWeek);
        dateMap.put("dateEnd", dateEndWeek);
        Long thisWeekVisitsNumber = orderMapper.findCountInDate(dateMap);
        reportData.put("thisWeekVisitsNumber", thisWeekVisitsNumber);

        //11. 查询本月到诊数
        dateMap.put("dateBegin", dateBeginMonth);
        dateMap.put("dateEnd", dateEndMonth);
        Long thisMonthVisitsNumber = orderMapper.findCountInDate(dateMap);
        reportData.put("thisMonthVisitsNumber", thisMonthVisitsNumber);

        //12. 查询热门套餐,取前四个
        ArrayList<HashMap<String, Object>> hotSetmeal = setMealMapper.findHotSetMeal();
        reportData.put("hotSetmeal", hotSetmeal);

        return reportData;
    }

    @Override
    public HashMap<String, Object> getMemberInfo() throws Exception {
        HashMap<String, Object> map = new HashMap<>();

        List<Map<String, String>> sexProList = memberMapper.findSex();
        ArrayList<String> sexList = new ArrayList<>();

        for (Map<String, String> sexMap : sexProList) {
            sexList.add(sexMap.get("name"));
        }

        map.put("sexProList", sexProList);
        map.put("sexList", sexList);

        return map;
    }

    @Override
    public HashMap<String, Object> getMemberAgeInfo(Map<String, String> ageMap) {
        HashMap<String, Object> map = new HashMap<>();

        ArrayList<String> ageList = new ArrayList<>();

        ArrayList<Map<String, Object>> ageProList = new ArrayList<>();

        Set<String> beginAges = ageMap.keySet();
        for (String beginAge : beginAges) {

            String endAge = ageMap.get(beginAge);
            String name = beginAge + "-" + endAge + " 岁";
            ageList.add(name);

            HashMap<String, String> tempMap = new HashMap<>();
            tempMap.put("beginAge",beginAge);
            tempMap.put("endAge",endAge);
            Integer value = memberMapper.getMemberAgeInfo(tempMap);

            HashMap<String, Object> ageProMap = new HashMap<>();
            ageProMap.put("value",value);
            ageProMap.put("name",name);
            ageProList.add(ageProMap);
        }
        map.put("ageList",ageList);
        map.put("ageProList",ageProList);

        return map;
    }
}
