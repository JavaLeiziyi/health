package com.lzy.Service;

import com.lzy.entity.PageResult;
import com.lzy.entity.QueryPageBean;
import com.lzy.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    List<Menu> findMenu(String username);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Menu> findLevel(String level);

    void add(Menu menu, Integer[] parentIds, Integer[] childrenIds);

    void delete(Integer id) throws Exception;

    Map<String, Object> findMenuByParentMenuId(Integer parentMenuId) throws Exception;

    List<Integer> findParentIdsById(Integer id);

    void editParentMenu(Menu menu, Integer[] childrenIds) throws Exception;

    void editChildrenMenu(Menu menu, Integer[] parentIds);
}
