package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * 商铺类型服务实现类，提供商铺类型的查询功能，支持缓存
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {
        // 1. 从 redis 查询商户类型缓存
        String key = CACHE_SHOP_TYPE_KEY;
        String shopTypeJson = stringRedisTemplate.opsForValue().get(key);
        // 2. 存在，直接返回
        if (StrUtil.isNotBlank(shopTypeJson)) {
            List<ShopType> typeList = JSONUtil.toList(shopTypeJson, ShopType.class);
            return Result.ok(typeList);
        }
        // 3. 不存在，查数据库
        List<ShopType> typeList = query().orderByAsc("sort").list();
        // 4. 还是不存在，返回错误
        if (typeList == null) {
            return Result.fail("店铺类型不存在");
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(typeList), 30L, TimeUnit.MINUTES);

        return Result.ok(typeList);
    }
}
