package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lzy.Service.RoleService;
import com.lzy.mapper.RoleMapper;
import com.lzy.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRoles() {
        return roleMapper.findRoles();
    }
}
