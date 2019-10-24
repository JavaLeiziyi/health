package com.lzy.jobs;

import com.lzy.constant.RedisConstant;
import com.lzy.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义的定时清理图片的Job
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        //根据Redis中保存的两个Set集合进行差值计算
        Set<String> stringSet = jedisPool.getResource().sdiff(
                RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (stringSet != null && stringSet.size() > 0) {
            System.out.println("清理开始: ");
            for (String fileName : stringSet) {
                //清理七牛云上无效的图片
                QiniuUtils.deleteFileFromQiniu(fileName);
                //清理redis集合中的图片
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
                System.out.println("    "+fileName + "被清理了!");
            }
        }
    }
}
