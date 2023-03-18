package com.huliua.miniprogram.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.huliua.miniprogram.annotation.CheckAuth;
import com.huliua.miniprogram.constant.CommonConstants;
import com.huliua.miniprogram.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    // 签名过期时间
    public static final long EXPIRE_TIME = 60L * 60 * 1000 * 25 * 365;
    // 随机生成密钥
    private static final String SECRET = UUID.randomUUID().toString();
    @Resource
    private RedisTemplate redisTemplate;

    /***
     * 校验权限，用于注解@CheckAuth使用
     * 
     * @param request
     */
    public boolean checkAuth(CheckAuth checkAuth, HttpServletRequest request) {
        // 不用登录，则放行
        if (!checkAuth.needLogin()) {
            return true;
        }
        if (null == request) {
            return false;
        }

        // 检验token有效性
        String token = request.getHeader("token");
        if (!verifyToken(token)) {
            return false;
        }

        // 获取用户信息
        User user = getUserByToken(token);
        // 校验redis中token有效期
        if (!redisTemplate.hasKey(CommonConstants.redis_prefix_login + user.getUserId())) {
            return false;
        }
        // 续期
        redisTemplate.expire(CommonConstants.redis_prefix_login + user.getUserId(), 60 * 30, TimeUnit.SECONDS);
        // 校验auth
        return validateAuth(checkAuth.auth(), user);
    }

    /**
     * 根据token校验用户权限
     * @param authArr 可访问的权限
     * @param token token
     * @return 如果用户的权限满足可访问的权限，返回true；否则返回false
     */
    private static boolean validateAuthByToken(String[] authArr, String token) {
        if (null == authArr || authArr.length == 0) {
            return true;
        }
        User user = getUserByToken(token);
        return validateAuth(authArr, user);
    }

    private static boolean validateAuth(String[] authArr, User user) {
        if (authArr.length == 0) {
            return true;
        }
        if (StrUtil.isEmpty(user.getAuth())) {
            return false;
        }
        return Arrays.asList(authArr).indexOf(user.getAuth()) >= 0;
    }

    /**
     * 生成token
     *
     * @return
     */
    public static String genToken(User user) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return JWT.create().withClaim("userId", user.getUserId()).withClaim("username", user.getUsername())
            .withClaim("wid", user.getWid()).withClaim("auth", user.getAuth()).withExpiresAt(expireDate)
            .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 获取token内的携带的用户信息
     *
     * @param token token
     * @return 用户信息
     */
    public static User getUserByToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        User user = new User();
        user.setUserId(decodedJWT.getClaim("userId").asString());
        user.setUsername(decodedJWT.getClaim("username").asString());
        user.setWid(decodedJWT.getClaim("wid").asString());
        user.setAuth(decodedJWT.getClaim("auth").asString());
        return user;
    }

    /**
     * 校验token是否合法
     *
     * @param token
     */
    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /***
     * 校验token合法性
     * 
     * @param request
     */
    public static boolean verifyToken(HttpServletRequest request) {
        if (null == request) {
            return false;
        }
        String token = request.getHeader("token");
        return verifyToken(token);
    }
}
