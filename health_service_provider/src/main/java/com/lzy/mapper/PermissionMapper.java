package com.lzy.mapper;

import com.lzy.pojo.Permission;

import java.util.Set;

public interface PermissionMapper {

    Set<Permission> findPermissionByRoleId(Integer id);
}
