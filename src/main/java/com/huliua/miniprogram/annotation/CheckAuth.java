package com.huliua.miniprogram.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuth {

    /**
     * 是否需要登录
     * @return
     */
    boolean needLogin() default true;

    /**
     * 可以访问的权限名
     * @return
     */
    String[] auth() default {""};
}
