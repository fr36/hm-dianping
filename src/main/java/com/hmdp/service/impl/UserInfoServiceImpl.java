package com.hmdp.service.impl;

import com.hmdp.entity.UserInfo;
import com.hmdp.mapper.UserInfoMapper;
import com.hmdp.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * 用户信息服务实现类，提供用户信息的查询、更新等功能
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
