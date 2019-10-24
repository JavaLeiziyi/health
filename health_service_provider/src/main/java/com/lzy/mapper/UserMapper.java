package com.lzy.mapper;

import com.lzy.pojo.User;

public interface UserMapper {
    User findUserByUsername(String username);
}
