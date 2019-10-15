package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzy.Service.CheckItemService;
import com.lzy.entity.PageResult;
import com.lzy.mapper.CheckItemMapper;
import com.lzy.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemMapper;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        if (queryString != null && queryString.length() > 0)
            queryString = "%" + queryString + "%";
        Page<CheckItem> page = checkItemMapper.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(CheckItem checkItem) throws RuntimeException {
        checkItemMapper.save(checkItem);
    }

    @Override
    public Long findCountByCheckItemId(Integer id) {
        return checkItemMapper.findCountByCheckItemId(id);
    }

    @Override
    public void delete(Integer id) {
        checkItemMapper.delete(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemMapper.edit(checkItem);
    }
}
