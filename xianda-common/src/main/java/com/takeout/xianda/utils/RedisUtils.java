package com.takeout.xianda.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //设置带过期时间的key-value
    public void set(String key, Object value, long time, TimeUnit unit ) {
        redisTemplate.opsForValue().set(key, value, time, unit);

    }
    //获取值
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    //删除key
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    //判断key是否存在
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
