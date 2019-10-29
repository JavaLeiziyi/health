package com.lzy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.MemberService;
import com.lzy.Service.OrderService;
import com.lzy.Service.ReportService;
import com.lzy.Service.SetMealService;
import com.lzy.constant.MessageConstant;
import com.lzy.entity.Result;
import com.lzy.pojo.Setmeal;
import com.lzy.utils.DateUtils;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setMealService;

    @Reference
    private OrderService orderService;

    @Reference
    private ReportService reportService;

    //展示每个年龄段的会员的占比
    @RequestMapping("/getMemberAgeInfo")
    private Result getMemberAgeInfo() {
        try {
            Map<String, String> ageMap = new HashMap<>();
            ageMap.put("0", "18");
            ageMap.put("19", "30");
            ageMap.put("31", "45");
            ageMap.put("46", "0");
            HashMap<String, Object> map = reportService.getMemberAgeInfo(ageMap);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //展示男女会员的占比
    @RequestMapping("/getMemberSexInfo")
    private Result getMemberInfo() {
        try {
            HashMap<String, Object> map = reportService.getMemberInfo();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //展示过去一年 时间内每个月的会员总数据量
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(@RequestBody List<String> list) {
        try {
            //定义存放月份和每月会员数量的集合
            Map<String, List> map = new HashMap<>();

            //1. 指定月份的集合
            List<String> months = DateUtils.getMonthBetween(list.get(0), list.get(1), "yyyy-MM");

            //2. 指定会员数量的集合
            ArrayList<Long> memberCount = new ArrayList<>();

            /*//2. 获取当前日期
            Calendar calendar = Calendar.getInstance();

            //3. 返回12月之前的日期
            calendar.add(Calendar.MONTH, -12);*/

            for (String month : months) {
                Long count = memberService.getMemberByDate(month);
                memberCount.add(count);
            }

            map.put("months", months);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //展示出会员预约的各个套餐占比情况
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            //1. 封装套餐名称以及对应预约数量
            HashMap<String, Object> map = new HashMap<>();
            //2. 封装套餐名称的集合
            ArrayList<String> setmealNames = new ArrayList<>();
            //3. 封装套餐名称以及对应预约数量的集合
            ArrayList<Map<Object, Object>> setmealCount = new ArrayList<>();

            //2. 查询所有套餐
            List<Setmeal> setmealList = setMealService.findAll();
            //3. 遍历套餐集合
            if (setmealList != null && setmealList.size() > 0) {
                for (Setmeal setmeal : setmealList) {
                    Map<Object, Object> setmealMap = new HashMap<>();
                    //4. 套餐名称添加到集合
                    setmealNames.add(setmeal.getName());
                    //5. 根据套餐id查询器所对应的预约数量
                    Long count = orderService.findCountBysetmealId(setmeal.getId());
                    //6. 套餐名称和预约数量添加到集合
                    setmealMap.put("name", setmeal.getName());
                    setmealMap.put("value", count);
                    setmealCount.add(setmealMap);
                }
                map.put("setmealNames", setmealNames);
                map.put("setmealCount", setmealCount);
            }
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    //展示出体检机构的运营情况
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            HashMap<String, Object> reportData = reportService.getReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    //导出本月体检机构的运营情况
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取远程调用的运营数据
            HashMap<String, Object> reportData = reportService.getReportData();
            String reportDate = (String) reportData.get("reportDate");
            Long todayNewMember = (Long) reportData.get("todayNewMember");
            Long totalMember = (Long) reportData.get("totalMember");
            Long thisWeekNewMember = (Long) reportData.get("thisWeekNewMember");
            Long thisMonthNewMember = (Long) reportData.get("thisMonthNewMember");
            Long todayOrderNumber = (Long) reportData.get("todayOrderNumber");
            Long todayVisitsNumber = (Long) reportData.get("todayVisitsNumber");
            Long thisWeekOrderNumber = (Long) reportData.get("thisWeekOrderNumber");
            Long thisWeekVisitsNumber = (Long) reportData.get("thisWeekVisitsNumber");
            Long thisMonthOrderNumber = (Long) reportData.get("thisMonthOrderNumber");
            Long thisMonthVisitsNumber = (Long) reportData.get("thisMonthVisitsNumber");
            ArrayList<HashMap<String, Object>> hotSetmeal = (ArrayList<HashMap<String, Object>>) reportData.get("hotSetmeal");

            //获取excel文件模板的绝对路径
            //File.separator 在不同操作系统可以使用 在windows中为"/"
            String fileURL = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //创建excel的工作簿对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(fileURL)));
            //获取工作表对象
            XSSFSheet sheet = workbook.getSheetAt(0);

            //获取第3行对象
            XSSFRow row3 = sheet.getRow(2);
            //设置第6个单元格的值
            row3.getCell(5).setCellValue(reportDate);

            //获取第5行对象
            XSSFRow row5 = sheet.getRow(4);
            //设置第6个单元格的值
            row5.getCell(5).setCellValue(todayNewMember);
            //设置第8个单元格的值
            row5.getCell(7).setCellValue(totalMember);

            //获取第6行对象
            XSSFRow row6 = sheet.getRow(5);
            //设置第6个单元格的值
            row6.getCell(5).setCellValue(thisWeekNewMember);
            //设置第8个单元格的值
            row6.getCell(7).setCellValue(thisMonthNewMember);

            //获取第8行对象
            XSSFRow row8 = sheet.getRow(7);
            //设置第6个单元格的值
            row8.getCell(5).setCellValue(todayOrderNumber);
            //设置第8个单元格的值
            row8.getCell(7).setCellValue(todayVisitsNumber);

            //获取第9行对象
            XSSFRow row9 = sheet.getRow(8);
            //设置第6个单元格的值
            row9.getCell(5).setCellValue(thisWeekOrderNumber);
            //设置第8个单元格的值
            row9.getCell(7).setCellValue(thisWeekVisitsNumber);

            //获取第10行对象
            XSSFRow row10 = sheet.getRow(9);
            //设置第6个单元格的值
            row10.getCell(5).setCellValue(thisMonthOrderNumber);
            //设置第8个单元格的值
            row10.getCell(7).setCellValue(thisMonthVisitsNumber);

            //遍历热门套餐的集合
            if (hotSetmeal != null && hotSetmeal.size() > 0) {
                for (int i = 0; i < hotSetmeal.size(); i++) {
                    HashMap<String, Object> map = hotSetmeal.get(i);
                    Object name = map.get("name");
                    Object setmeal_count = map.get("setmeal_count");
                    Object proportion = map.get("proportion");
                    Object attention = map.get("attention");

                    XSSFRow rowI = sheet.getRow(12 + i);
                    rowI.getCell(4).setCellValue(name.toString());
                    rowI.getCell(5).setCellValue(setmeal_count.toString());
                    rowI.getCell(6).setCellValue(proportion.toString());
                    rowI.getCell(7).setCellValue(attention.toString());
                }
            }

            //通过response流对象响应工作簿
            ServletOutputStream out = response.getOutputStream();
            //设置mime格式
            response.setContentType("application/vnd.ms-excel");
            //设置文件名
            String fileName = "report_template_" + reportDate + ".xlsx";
            //设置响应头信息,避免浏览器自动打开文件
            response.setHeader("content-Disposition", "attachment;filename=" + fileName);
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.BUSINESS_EXPORT_FAIL);
        }
        return new Result(true, MessageConstant.BUSINESS_EXPORT_SUCCESS);
    }
}