package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzy.Service.SetMealService;
import com.lzy.constant.RedisConstant;
import com.lzy.entity.PageResult;
import com.lzy.mapper.CheckGroupMapper;
import com.lzy.mapper.SetMealMapper;
import com.lzy.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        if (queryString != null && queryString.length() > 0) {
            queryString = "%" + queryString + "%";
        }
        Page<Setmeal> setMealPage = setMealMapper.findCondition(queryString);
        return new PageResult(setMealPage.getTotal(), setMealPage.getResult());
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealMapper.addSetMeal(setmeal);
        addGroupIdsBySetMealId(setmeal.getId(), checkGroupIds);
        //图片保存到数据库成功, 把图片信息保存到redis, 不影响原来业务逻辑地执行
        try {
            addRedis(setmeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //把图片信息保存到redis
    @Override
    public void addRedis(String fileName){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    @Override
    public void addGroupIdsBySetMealId(Integer id, Integer[] checkGroupIds) {
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            for (Integer checkGroupId : checkGroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkGroupId", checkGroupId);
                map.put("setMealId", id);
                setMealMapper.addGroupIdsBySetMealId(map);
            }
        }
    }

    @Override
    public List<Integer> findGroupIdsBySetMealId(Integer setMealId) {
        return setMealMapper.findGroupIdsBySetMealId(setMealId);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        //编辑数据库数据之前,先获取imgFileName, 并在redis删除
        try {
            Setmeal setMeal = findSetMealById(setmeal.getId());
            deleteRedis(setMeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setMealMapper.deleteSetMealAndCheckGroup(setmeal.getId());
        addGroupIdsBySetMealId(setmeal.getId(), checkGroupIds);
        setMealMapper.updateSetMeal(setmeal);

        //编辑完数据库之后, 重新把imgFileName保存到redis中
        try {
            addRedis(setmeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据setMealId获取imgFileName
    @Override
    public Setmeal findSetMealById(Integer id){
        return setMealMapper.findSetMealById(id);
    }

    //在redis删除imgFileName
    @Override
    public void deleteRedis(String fileName){
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    @Override
    public void delete(Integer setMealId) {
        try {
            //编辑数据库数据之前,先获取imgFileName, 并在redis删除
            Setmeal setMeal = findSetMealById(setMealId);
            deleteRedis(setMeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setMealMapper.deleteSetMealAndCheckGroup(setMealId);
        setMealMapper.deleteSetMealById(setMealId);
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealMapper.findAllSetMeal();
    }
    @Override
    public Setmeal findById(Integer setMealId) {
        //查询setMeal数据
        return setMealMapper.findById(setMealId);
    }
}
