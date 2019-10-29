package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzy.Service.UserService;
import com.lzy.entity.PageResult;
import com.lzy.mapper.UserMapper;
import com.lzy.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize,
                               String queryString) throws Exception {
        PageHelper.startPage(currentPage, pageSize);
        if (queryString != null) {
            queryString = "%" + queryString + "%";
        }
        Page<User> pageUser = userMapper.findPage(queryString);
        return new PageResult(pageUser.getTotal(), pageUser.getResult());
    }
    @Override
    public void add(User user, Integer[] roleIds) {
        userMapper.add(user);
        for (Integer roleId : roleIds) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("roleId", roleId);
            userMapper.addUserIdByRoleId(map);
        }
    }

    @Override
    public void edit(User user, Integer[] roleIds) {
        //删除中间表的关联关系
        userMapper.deleteRoleIdByUserId(user.getId());

        //为中间重新建立关联
        for (Integer roleId : roleIds) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("roleId", roleId);
            userMapper.addUserIdByRoleId(map);
        }

        //修改表
        userMapper.edit(user);
    }

    @Override
    public void delete(Integer userId) {
        //删除中间表的关联关系
        userMapper.deleteRoleIdByUserId(userId);

        //删除用户
        userMapper.delete(userId);
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userMapper.findRoleIdsByUserId(id);
    }
}
