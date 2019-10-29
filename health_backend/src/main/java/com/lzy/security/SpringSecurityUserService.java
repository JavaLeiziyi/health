package com.lzy.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.UserService;
import com.lzy.pojo.Permission;
import com.lzy.pojo.Role;
import com.lzy.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Set;

public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询username指定的用户信息
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return null;
        }

        //获取用户对应的角色
        Set<Role> roles = user.getRoles();
        //用户角色封装到"权限(GrantedAuthority)"的list集合
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        for (Role role : roles) {
            //授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //获取角色相对应的权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                list
        );
        return userDetails;
    }
}
