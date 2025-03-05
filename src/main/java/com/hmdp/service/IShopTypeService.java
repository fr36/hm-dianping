package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * 商铺类型服务接口，定义商铺类型的查询功能
 */
public interface IShopTypeService extends IService<ShopType> {

    Result queryTypeList();
}
