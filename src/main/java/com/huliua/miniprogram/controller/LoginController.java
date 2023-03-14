package com.huliua.miniprogram.controller;

import com.huliua.miniprogram.annotation.CatchException;
import com.huliua.miniprogram.annotation.CheckAuth;
import com.huliua.miniprogram.constant.AuthConstants;
import com.huliua.miniprogram.constant.CommonConstants;
import com.huliua.miniprogram.entity.ResponseResult;
import com.huliua.miniprogram.entity.User;
import com.huliua.miniprogram.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CatchException
@Log4j2
@RestController
@RequestMapping("/login/")
@Api(value = "登录接口", tags = {"登录接口"})
public class LoginController {

    @Resource
    private UserService userService;

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
            log.error("LoginController。getUserById error:", ex);
            result.setCode(CommonConstants.response_code_error);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    @CheckAuth(auth = {AuthConstants.AUTH_ADMIN, AuthConstants.AUTH_USER})
    @RequestMapping("/test")
    public ResponseResult doLogin(User user) throws Exception {
        throw new Exception("sss");
    }
}
