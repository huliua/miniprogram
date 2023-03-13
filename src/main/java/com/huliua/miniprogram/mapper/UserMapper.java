package com.huliua.miniprogram.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huliua.miniprogram.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
