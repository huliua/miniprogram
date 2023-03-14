package com.huliua.miniprogram.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.huliua.miniprogram.annotation.CheckAuth;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AuthUtils {

    private static final String SING = "huliua";

    /***
     * 校验token合法性
     * @param request
     */
    public static boolean checkToken(HttpServletRequest request) {
        if (null == request) {
            return false;
        }
        String token = request.getHeader("token");
        return StringUtils.hasLength(token);
    }

    /***
     * 校验token合法性
     * @param request
     */
    public static boolean checkToken(CheckAuth checkAuth, HttpServletRequest request) {
        // 不用登录，则放行
        if (!checkAuth.needLogin()) {
            return true;
        }
        if (null == request) {
            return false;
        }
        String token = request.getHeader("token");
        // 检验token有效性
        return validateToken(checkAuth.auth(), token);
    }

    /**
     * 校验token的有效性
     */
    public static boolean validateToken(String[] auth, String token) {
        // 校验token的合法性
        if (!validateToken(token)) {
            return false;
        }
        // 校验token的权限
        return !validateAuthByToken(auth, token);
    }

    /**
     * 校验token的有效性
     */
    public static boolean validateToken(String token) {
        // 请求头中没有token则禁止访问
        return StringUtils.hasLength(token);
    }

    public static boolean validateAuthByToken(String[] authArr, String token) {
        if (null == authArr || authArr.length == 0) {
            return true;
        }
        String auth = getAuthByToken(token);
        return true;
    }

    public static String getAuthByToken(String token) {
        Map<String, Claim> tokenInfo = getTokenInfo(token);
        return tokenInfo.get("auth").asString();
    }

    /**
     * 生成token
     *
     * @param map
     * @return
     */
    public static String genToken(HashMap<String, String> map) {
        //获取日历对象
        Calendar calendar = Calendar.getInstance();
        //默认1小时过期
        calendar.add(Calendar.HOUR, 1);
        //新建一个JWT的Builder对象
        JWTCreator.Builder builder = JWT.create();
        //将map集合中的数据设置进payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        //设置过期时间和签名
        return builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SING));
    }

    /**
     * 验签并返回DecodedJWT
     *
     * @param token 令牌
     * @return
     */
    public static Map<String, Claim> getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token).getClaims();
    }
}
