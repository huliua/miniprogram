package com.huliua.miniprogram.controller;

import cn.hutool.core.util.StrUtil;
import com.huliua.miniprogram.annotation.CatchException;
import com.huliua.miniprogram.annotation.CheckAuth;
import com.huliua.miniprogram.constant.CommonConstants;
import com.huliua.miniprogram.entity.BusinessException;
import com.huliua.miniprogram.entity.ResponseResult;
import com.huliua.miniprogram.entity.User;
import com.huliua.miniprogram.service.UserService;
import com.huliua.miniprogram.utils.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@CatchException
@Log4j2
@RestController
@RequestMapping("/login/")
@Api(value = "登录接口", tags = {"登录接口"})
public class LoginController {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @CheckAuth
    @ApiOperation(value = "根据用户id获取用户信息", response = ResponseResult.class)
    @GetMapping("getUserById")
    public ResponseResult getUserById(@ApiParam(value = "用户id", required = true) String userId) {
        ResponseResult result = new ResponseResult();
        result.setCode(CommonConstants.response_code_success);
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                result.setCode(CommonConstants.response_code_fail);
                result.setMsg("未获取到用户信息！");
                return result;
            }
            result.setDatas(user);
        } catch (Exception ex) {
            log.error("LoginController.getUserById error:", ex);
            result.setCode(CommonConstants.response_code_error);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "登录接口", response = ResponseResult.class)
    @CheckAuth(needLogin = false)
    @RequestMapping("/doLogin")
    public ResponseResult doLogin(@ApiParam(value = "用户信息", required = true)User param) throws BusinessException {
        assert param != null;
        ResponseResult result = new ResponseResult();
        // 校验用户名密码是否填写
        if (StrUtil.isEmpty(param.getUserId()) || StrUtil.isEmpty(param.getPassword())) {
            result.setCode(CommonConstants.response_code_fail);
            result.setMsg("请输入账号和密码！");
            return result;
        }

        // 获取用户信息
        User user = userService.getUser(param);
        // 生成token，并存储用户信息到redis中
        String token = AuthUtils.genToken(user);

        // token默认有效期30分钟
        redisTemplate.opsForValue().set(CommonConstants.redis_prefix_login + user.getUserId(), token, 60 * 30, TimeUnit.SECONDS);

        result.setCode(CommonConstants.response_code_success);
        result.setMsg("登录成功！");
        result.setDatas(token); // 返回token
        return result;
    }
}
