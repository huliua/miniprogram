package com.huliua.miniprogram.service;

import com.huliua.miniprogram.entity.BusinessException;
import com.huliua.miniprogram.entity.User;

public interface UserService {

    User getUserById(String userId);

    User getUser(User user) throws BusinessException;
}
