package com.huliua.miniprogram.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huliua.miniprogram.constant.CommonConstants;
import com.huliua.miniprogram.entity.BusinessException;
import com.huliua.miniprogram.entity.User;
import com.huliua.miniprogram.mapper.UserMapper;
import com.huliua.miniprogram.service.UserService;
import com.huliua.miniprogram.utils.PasswordUtils;

import cn.hutool.core.util.StrUtil;

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
        queryWrapper.eq(StrUtil.isNotEmpty(param.getUserId()), "userId", param.getUserId());
        queryWrapper.eq(StrUtil.isNotEmpty(param.getWid()), "wid", param.getWid());
        queryWrapper.eq(StrUtil.isNotEmpty(param.getPhone()), "phone", param.getPhone());
        queryWrapper.eq(StrUtil.isNotEmpty(param.getEmail()), "email", param.getEmail());
        User user = userMapper.selectOne(queryWrapper);
        if (null == user) {
            throw new BusinessException(CommonConstants.response_code_fail, "用户不存在！");
        }
        // 如果需要检验密码
        if (StrUtil.isNotEmpty(param.getPassword()) && !PasswordUtils.matches(param.getPassword(), user.getPassword())) {
            throw new BusinessException(CommonConstants.response_code_fail, "密码错误！");
        }
        return user;
    }
}
