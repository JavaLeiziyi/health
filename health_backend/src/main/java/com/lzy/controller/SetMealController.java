package com.lzy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.SetMealService;
import com.lzy.constant.MessageConstant;
import com.lzy.entity.PageResult;
import com.lzy.entity.QueryPageBean;
import com.lzy.entity.Result;
import com.lzy.pojo.Setmeal;
import com.lzy.utils.QiniuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setmealService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile) {
        try {
            //获取原始文件名称
            String originalFilename = multipartFile.getOriginalFilename();
            //filename="03a36073-a140-4942-9b9b-712cecb144901.jpg"
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀.jpg
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            //上传文件
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(), fileName);
            //图片上传成功
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findGroupIdsBySetMealId/{setMealId}")
    public Result findGroupIdsBySetMealId(@PathVariable("setMealId") Integer setMealId) {
        try {
            List<Integer> groupIds = setmealService.findGroupIdsBySetMealId(setMealId);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, groupIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.edit(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/delete/{setMealId}")
    public Result delete(@PathVariable("setMealId") Integer setMealId) {
        try {
            setmealService.delete(setMealId);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }
}
