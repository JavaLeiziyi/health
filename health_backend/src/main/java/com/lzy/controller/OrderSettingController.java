package com.lzy.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.OrderSettingService;
import com.lzy.constant.MessageConstant;
import com.lzy.entity.Result;
import com.lzy.pojo.OrderSetting;
import com.lzy.utils.POIUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 上传Excel文件, 并解析文件内容保存到数据库
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() > 0) {
                //存储解析后的数据
                ArrayList<OrderSetting> orderSettingList = new ArrayList<>();
                //读取Excel文件按的每一行数据
                for (String[] strings : list) {
                    //解析每一列的值,第一列为date类型, 第二列为int类型
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    orderSettingList.add(orderSetting);
                }
                //解析后的数据保存到数据库
                orderSettingService.add(orderSettingList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderByMonth")
    public Result getOrderByMonth(String date) {//2019-10
        try {
            List<Map> mapList = orderSettingService.getOrderByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }

    /**
     * 导出Excel文件
     *
     * @param fileName
     * @param orderDate
     * @param response
     * @return
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(String fileName, String orderDate, HttpServletResponse response) {
        try {
            List<OrderSetting> orderSettingList = orderSettingService.exportExcel(orderDate);

            XSSFWorkbook sheets = null;
            if (orderSettingList != null && orderSettingList.size() > 0) {
                //把数据写入excel表中
                //1. 在内存创建Excel文件对象
                sheets = new XSSFWorkbook();
                //2. 创建工作表,指定工作表的名称
                XSSFSheet sheet = sheets.createSheet("预约数据");
                for (int i = 0; i < orderSettingList.size(); i++) {
                    //3. 创建行, 从第一行开始
                    XSSFRow row = sheet.createRow(i);

                    //4. 创建单元格, 从第一个单元格开始
                    if (i == 0) {
                        row.createCell(0).setCellValue("日期");
                        row.createCell(1).setCellValue("可预约数量");
                    } else {
                        //5. 给每一个单元格添加值
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        String date = format.format(orderSettingList.get(i).getOrderDate());
                        row.createCell(0).setCellValue(date);
                        row.createCell(1).setCellValue((orderSettingList.get(i).getNumber()));
                    }
                }
            }
            //excel文件对象通过流来写出去
            loadResponse(fileName, response, sheets, orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将excel文件对象通过流来写出去
    public void loadResponse(String excelName, HttpServletResponse response, XSSFWorkbook wb, String date) throws IOException {
        //创建内存数组字节流对象
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //将excel写入流
        wb.write(byteArrayOutputStream);
        //设置文件标题
        String outFile = excelName + date + ".xlsx";
        //设置返回的文件类型excel
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");xls
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //对文件编码
        outFile = response.encodeURL(new String(outFile.getBytes("gb2312"), "iso8859-1"));
        //使用Servlet实现文件下载的时候，避免浏览器自动打开文件
        response.addHeader("Content-Disposition", "attachment;filename=" + outFile);
        //设置文件大小
        response.setContentLength(byteArrayOutputStream.size());
        //创建Cookie并添加到response中
        Cookie cookie = new Cookie("fileDownload", "true");
        cookie.setPath("/");
        response.addCookie(cookie);
        //将流写进response输出流中
        ServletOutputStream sos = response.getOutputStream();
        byteArrayOutputStream.writeTo(sos);
        //不要关闭输出流
        byteArrayOutputStream.close();
        sos.flush();
    }
}
