package com.huliua.miniprogram.service.impl;

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
}
