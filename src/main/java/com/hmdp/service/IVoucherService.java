package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * 优惠券服务接口，定义优惠券的添加、查询等功能
 */
public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);

}
