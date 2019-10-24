package com.lzy.Service;

import com.lzy.pojo.User;

public interface UserService {
    User findUserByUsername(String username);
}
