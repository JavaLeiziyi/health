package com.lzy.Service;

import com.lzy.entity.PageResult;
import com.lzy.pojo.Role;
import com.lzy.pojo.User;

import java.util.List;

public interface UserService {
    User findUserByUsername(String username);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception;

    void delete(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void add(User user, Integer[] roleIds);

    void edit(User user, Integer[] roleIds);
}
