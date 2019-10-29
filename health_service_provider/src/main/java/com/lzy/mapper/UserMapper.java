package com.lzy.mapper;

import com.github.pagehelper.Page;
import com.lzy.pojo.User;

import java.util.HashMap;
import java.util.List;

public interface UserMapper {
    User findUserByUsername(String username);

    Page<User> findPage(String queryString);

    void add(User user);

    void addUserIdByRoleId(HashMap<String, Integer> map);

    void delete(Integer userId);

    void deleteRoleIdByUserId(Integer userId);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user);
}
