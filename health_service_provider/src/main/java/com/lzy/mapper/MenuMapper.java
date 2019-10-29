package com.lzy.mapper;

import com.github.pagehelper.Page;
import com.lzy.pojo.Menu;

import java.util.HashMap;
import	java.util.List;


public interface MenuMapper {
    List<Menu> findLevel1Menu(String username);

    List<Menu> findLevel2Menu(Integer id);

    Page<Menu> findCondition(String queryString);

    List<Menu> findLevel(String level);

    void add(Menu menu);

    void addParentIdById(HashMap<String, Integer> map);

    void delete(Integer id);

    List<Integer> selectIdBParentId(Integer parentMenuId);

    List<Menu> findMenuByParentMenuId(Integer parentMenuId);

    List<Integer> findParentIdsById(Integer id);

    void updateParentMenuIdById(Integer id);

    void editParentMenu(Menu menu);
}
