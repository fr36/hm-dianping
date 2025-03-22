package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;


public class SimpleRedisLock implements ILock{

    private final String name;
    private final StringRedisTemplate stringRedisTemplate;
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";

    // setnx实现的分布式锁（已取消)

    /*
    基于setnx实现的分布式锁存在下面的问题：
    01
    不可重入
    同一个线程无法多次获取同一把锁
    02
    不可重试
    获取锁只尝试一次就返回
    false，没有重试机制
    03
    超时释放
    锁超时释放虽然可以避免死
    锁，但如果是业务执行耗时较长，也会导致锁释放，存在安全隐患
    04
    主从一致性
    如果Redis提供了主从集群，主从同步存在延迟，当主宕
    机时，如果从并同步主中的锁数据，则会出现锁实现
     */
    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    private static final String KEY_PREFIX = "lock:";

    @Override
    public boolean tryLock(Long timeSec) {
        String threadId = ID_PREFIX + Thread.currentThread().getId();

        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + name, threadId, timeSec, TimeUnit.SECONDS);

        return Boolean.TRUE.equals(success);
    }

    @Override
    public void unlock() {
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        if(threadId.equals(id)){
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }
}
