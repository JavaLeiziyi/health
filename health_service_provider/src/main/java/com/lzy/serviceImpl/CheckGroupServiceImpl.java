package com.lzy.serviceImpl;

import java.util.HashMap;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzy.Service.CheckGroupService;
import com.lzy.entity.PageResult;
import com.lzy.mapper.CheckGroupMapper;
import com.lzy.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupMapper.addCheckGroup(checkGroup);
        addCheckGroupAndItem(checkGroup.getId(), checkitemIds);
    }

    @Override
    public void addCheckGroupAndItem(Integer checkgroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", checkgroupId);
                map.put("checkitemId", checkitemId);
                checkGroupMapper.addCheckGroupAndItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        if (queryString != null && queryString.length() > 0) {
            queryString = "%" + queryString + "%";
        }
        Page<CheckGroup> groupPage = checkGroupMapper.findPage(queryString);
        return new PageResult(groupPage.getTotal(), groupPage.getResult());
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkGroupId) {
        return checkGroupMapper.findCheckItemIdsByCheckGroupId(checkGroupId);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先删除中间表GroupId对应的ItemId, 解除关联关系
        checkGroupMapper.deleteItemIdByGroupId(checkGroup.getId());
        //重新创建关联关系
        addCheckGroupAndItem(checkGroup.getId(), checkitemIds);
        //更新Group表
        checkGroupMapper.updateGroup(checkGroup);
    }

    @Override
    public void delete(Integer groupId) {
        //删除中间表的关联关系
        checkGroupMapper.deleteItemIdByGroupId(groupId);
        //删除group表
        checkGroupMapper.deleteCheckGroup(groupId);
    }
}
