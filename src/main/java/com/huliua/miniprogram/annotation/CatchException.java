package com.huliua.miniprogram.annotation;

import java.lang.annotation.*;

/**
 * 自动捕获异常注解
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CatchException {
}
