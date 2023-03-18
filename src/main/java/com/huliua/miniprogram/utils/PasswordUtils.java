package com.huliua.miniprogram.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 */
public class PasswordUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 加密
     * @param pwd 密码
     * @return 加密后的密码
     */
    public static String encode(String pwd) {
        return encoder.encode(pwd);
    }

    /**
     * 校验密码是否正确
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 比对成功（true）或失败（false）
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
