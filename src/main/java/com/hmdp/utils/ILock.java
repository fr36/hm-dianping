package com.hmdp.utils;

/**
 * 定义一个接口，用于实现分布式锁或同步锁的定制
 * 分布式锁是在分布式系统中实现数据同步的一种机制，确保在同一时间内只有一个进程可以对共享资源进行访问
 * 同步锁则是用于多线程环境下，确保同一时间内只有一个线程可以执行特定的代码块
 */
public interface ILock {

    /**
     * 尝试获取锁
     * 此方法允许调用者在指定的时间内尝试获取锁如果锁在指定时间内无法获取，那么方法将返回false
     *
     * @param timeSec 等待锁的时间，单位为秒如果为null或负数，则应抛出异常或以某种方式处理无效输入
     * @return 如果成功获取锁，则返回true；如果在指定时间内未获取到锁，则返回false
     * @throws IllegalArgumentException 如果timeSec为null或负数，应抛出此异常
     */
    boolean tryLock(Long timeSec);

    /**
     * 释放锁
     * 当操作完成，不再需要锁时，调用此方法来释放锁，以便其他线程或进程可以尝试获取锁
     */
    void unlock();
}

