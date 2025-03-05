package com.hmdp.controller;

import com.hmdp.dto.Result;

import com.hmdp.service.IShopTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * 商铺类型控制器，提供商铺类型的查询功能
 */
@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private IShopTypeService typeService;

    @GetMapping("list")
    public Result queryTypeList() {
        // List<ShopType> typeList = typeService
        // .query().orderByAsc("sort").list();
        //
        return typeService.queryTypeList();
    }
}
