package com.hmdp.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.utils.RedisConstants.CACHE_NULL_TTL;

@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 使用逻辑过期时间设置Redis键值对
     * 逻辑过期时间是指在Redis中存储的数据附带一个过期时间，而不是使用Redis自身的过期机制
     * 避免缓存雪崩
     *
     * @param key 存储在Redis中的键
     * @param value 存储在Redis中的值
     * @param time 过期时间
     * @param unit 过期时间的时间单位
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){
        // 创建一个RedisData对象，用于存储数据和过期时间
        RedisData redisData = new RedisData();
        // 设置需要存储的数据
        redisData.setData(value);
        // 计算并设置逻辑过期时间
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        // 将RedisData对象转换为JSON字符串并存储到Redis中
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 使用缓存穿透策略查询数据
     * 
     * @param keyPrefix 键前缀
     * @param id 数据ID
     * @param type 数据类型
     * @param dbFallback 数据库查询回调函数
     * @param time 过期时间
     * @param unit 过期时间的时间单位
     */
    public <R, ID>  R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(json)){
            return JSONUtil.toBean(json, type);
        }
        if(json != null){
            return null;
        }

        R r = dbFallback.apply(id);
        if(r == null){
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        this.set(key, r, time, unit);

        return r;
    }

}
