package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzy.Service.MenuService;
import com.lzy.entity.PageResult;
import com.lzy.entity.QueryPageBean;
import com.lzy.mapper.MenuMapper;
import com.lzy.pojo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> findMenu(String username) {
        //根据用户名在数据库中查询用户对应角色的菜单
        List<Menu> menuList = menuMapper.findLevel1Menu(username);
        return menuList;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        String queryString = queryPageBean.getQueryString();
        if (queryString != null && queryString.length() > 0) {
            queryString = "%" + queryString + "%";
        }
        Page<Menu> menus = menuMapper.findCondition(queryString);
        return new PageResult(menus.getTotal(), menus.getResult());
    }

    @Override
    public List<Menu> findLevel(String level) {
        return menuMapper.findLevel(level);
    }

    @Override
    public void add(Menu menu, Integer[] parentIds, Integer[] childrenIds) {

        //一级菜单
        if (menu.getLevel() == 1) {
            menuMapper.add(menu);
            if (childrenIds != null && childrenIds.length > 0) {
                for (Integer childrenId : childrenIds) {
                    HashMap<String, Integer> map = new HashMap<>();
                    map.put("id", childrenId);
                    map.put("parentMenuId", menu.getId());
                    menuMapper.addParentIdById(map);
                }
            }
        }
        //二级菜单
        if (menu.getLevel() == 2) {
            if (parentIds != null && parentIds.length > 0) {
                for (Integer parentId : parentIds) {
                    menu.setParentMenuId(parentId);
                }
            }
            menuMapper.add(menu);
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        menuMapper.delete(id);
    }

    @Override
    public Map<String, Object> findMenuByParentMenuId(Integer parentMenuId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        //查询本id已选的子菜单的id
        List<Integer> idList = menuMapper.selectIdBParentId(parentMenuId);
        map.put("idList", idList);

        //查询所有可选的子菜单(包括本id已选的子菜单)
        List<Menu> menuList = menuMapper.findMenuByParentMenuId(parentMenuId);
        map.put("menuList", menuList);

        return map;
    }

    @Override
    public List<Integer> findParentIdsById(Integer id) {
        return menuMapper.findParentIdsById(id);
    }

    @Override
    public void editParentMenu(Menu menu, Integer[] childrenIds) throws Exception {
        //删除父级菜单与所有子级菜单的关联
        menuMapper.updateParentMenuIdById(menu.getId());

        //重新关联菜单关系
        for (Integer childrenId : childrenIds) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("parentMenuId", menu.getId());
            map.put("id", childrenId);
            menuMapper.addParentIdById(map);
        }

        //编辑父级菜单
        menuMapper.editParentMenu(menu);
    }

    @Override
    public void editChildrenMenu(Menu menu, Integer[] parentIds) {
        //编辑子级菜单
        for (Integer parentId : parentIds) {
            menu.setParentMenuId(parentId);
        }
        menuMapper.editParentMenu(menu);
    }
}
