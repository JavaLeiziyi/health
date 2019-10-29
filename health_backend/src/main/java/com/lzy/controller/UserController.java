package com.lzy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.RoleService;
import com.lzy.Service.UserService;
import com.lzy.constant.MessageConstant;
import com.lzy.entity.PageResult;
import com.lzy.entity.QueryPageBean;
import com.lzy.entity.Result;
import com.lzy.pojo.Role;
import com.lzy.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Reference
    private RoleService roleService;

    @RequestMapping("/getUsername")
    public Result getUsername() {
        //在springSecurity的上下文(核心容器)中获取username
        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult userList = null;
        try {
            userList = userService.findPage(
                    queryPageBean.getCurrentPage(),
                    queryPageBean.getPageSize(),
                    queryPageBean.getQueryString()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @RequestMapping("/findRoles")
    public Result findRoles() {
        try {
            List<Role> roles = roleService.findRoles();
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roles);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody User user, Integer[] roleIds) {
        try {
            userService.add(user, roleIds);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    @RequestMapping("delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        try {
            userService.delete(id);
            return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
    }

    @RequestMapping("/findRoleIdsByUserId/{id}")
    public Result findRoleIdsByUserId(@PathVariable("id") Integer id) {
        try {
            List<Integer> roleIds = userService.findRoleIdsByUserId(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody User user, Integer[] roleIds) {
        try {
            userService.edit(user, roleIds);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
    }
}
