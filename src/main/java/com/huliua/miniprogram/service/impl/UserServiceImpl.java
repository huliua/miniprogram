package com.huliua.miniprogram.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huliua.miniprogram.constant.CommonConstants;
import com.huliua.miniprogram.entity.BusinessException;
import com.huliua.miniprogram.entity.User;
import com.huliua.miniprogram.mapper.UserMapper;
import com.huliua.miniprogram.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User getUserById(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User getUser(User param) throws BusinessException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", param.getUserId());
        queryWrapper.eq(StrUtil.isNotEmpty(param.getPassword()), "password", param.getPassword());
        queryWrapper.eq(StrUtil.isNotEmpty(param.getWid()), "wid", param.getWid());
        queryWrapper.eq(StrUtil.isNotEmpty(param.getPhone()), "phone", param.getPhone());
        queryWrapper.eq(StrUtil.isNotEmpty(param.getEmail()), "email", param.getEmail());
        return userMapper.selectOne(queryWrapper);
    }
}
