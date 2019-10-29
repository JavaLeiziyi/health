package com.lzy.controller;

import java.lang.reflect.Parameter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.MenuService;
import com.lzy.constant.MessageConstant;
import com.lzy.constant.RedisConstant;
import com.lzy.entity.PageResult;
import com.lzy.entity.QueryPageBean;
import com.lzy.entity.Result;
import com.lzy.pojo.Menu;
import com.lzy.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult list = menuService.findPage(queryPageBean);
        return list;
    }

    @RequestMapping("/findMenu")
    public Result findMenu(String username) {
        try {
            List<Menu> menuList = menuService.findMenu(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    @RequestMapping("/findLevel")
    public Result findLevel(String level) {
        try {
            List<Menu> menuList = menuService.findLevel(level);
            if (menuList == null || menuList.size() == 0) {
                return new Result(false, MessageConstant.QUERY_MENU_EMPTY);
            }
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }

    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取图片的后缀
            String originalFilename = imgFile.getOriginalFilename();
            int i = originalFilename.indexOf(".");
            String suffix = originalFilename.substring(i);
            //使用随机数拼接后缀
            String fileName = UUID.randomUUID() + suffix;
            //上传图片
            QiniuUtils.upload2Qiniu2(imgFile.getBytes(), fileName);
            //把图片信息存进Redis
            jedisPool.getResource().sadd(RedisConstant.MENU_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu, Integer[] parentIds, Integer[] childrenIds) {
        try {
            menuService.add(menu, parentIds, childrenIds);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    @RequestMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        try {
            menuService.delete(id);
            return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }

    @RequestMapping("/findMenuByParentMenuId")
    private Result findMenuByParentMenuId(Integer parentMenuId) {
        try {
            Map<String, Object> map = menuService.findMenuByParentMenuId(parentMenuId);
            if (map == null || map.size() == 0) {
                return new Result(false, MessageConstant.QUERY_MENU_EMPTY);
            }
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }

    @RequestMapping("/findParentIdsById")
    private Result findParentIdsById(Integer id) {
        try {
            List<Integer> parentIds = menuService.findParentIdsById(id);
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, parentIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }

    @RequestMapping("/edit")
    private Result edit(@RequestBody Menu menu, Integer[] parentIds, Integer[] childrenIds) {
        try {
            //父级菜单的编辑
            if (menu.getLevel() == 1) {
                menuService.editParentMenu(menu, childrenIds);
            }
            //子级菜单的编辑
            if (menu.getLevel() == 2) {
                menuService.editChildrenMenu(menu, parentIds);
            }
            return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MENU_FAIL);
        }
    }
}
