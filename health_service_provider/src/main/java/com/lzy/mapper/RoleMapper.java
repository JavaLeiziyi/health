package com.lzy.mapper;


import com.lzy.pojo.Role;

import java.util.List;
import java.util.Set;

public interface RoleMapper {

    Set<Role> findRoleByUserId(Integer id);

    List<Role> findRoles();
}
