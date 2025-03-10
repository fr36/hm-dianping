package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.CacheClient;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * 商铺服务实现类，提供商铺信息的查询、更新等功能，并实现了缓存处理
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result queryById(Long id) {

        // //缓存击穿
        // Shop shop = cacheClient.queryWithLogicalExpire(
        //         CACHE_SHOP_KEY, id,
        //         Shop.class,
        //         this::getById,
        //         10L,                                   
        //         TimeUnit.SECONDS
        // );
        // if (shop != null) {
        //     return Result.ok(shop);
            
        // }
        
        //缓存穿透阶段
        Shop shop = cacheClient.queryWithPassThrough(
                CACHE_SHOP_KEY,
                id,
                Shop.class,
                this::getById,
                10L,
                TimeUnit.MINUTES);
        
        if (shop == null) {
            return Result.fail("店铺不存在");
        }
        
        return Result.ok(shop);
    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺 id 不能为空");
        }
        // 1.更新数据库
        updateById(shop);
        // 2. 删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);

        return Result.ok();
    }
}
