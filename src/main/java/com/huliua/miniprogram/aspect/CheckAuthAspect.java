package com.huliua.miniprogram.aspect;

import com.huliua.miniprogram.annotation.CheckAuth;
import com.huliua.miniprogram.constant.CommonConstants;
import com.huliua.miniprogram.entity.ResponseResult;
import com.huliua.miniprogram.utils.AuthUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Aspect
@Component
public class CheckAuthAspect {

    @Pointcut("@annotation(com.huliua.miniprogram.annotation.CheckAuth)")
    public void pointCut() {}

    /**
     * 注意，这里是&&， 且@annotation括号中的名字，必须和before()中的参数名一样
     * @param annotation
     */
    @Around(value = "pointCut() && @annotation(annotation))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, CheckAuth annotation) throws Throwable {
        log.info("start checkAuth, param:[needLogin:{}, auth: {}]", annotation.needLogin(), annotation.auth());
        ResponseResult result = null;
        log.info("Signature:{}, Args:{}", proceedingJoinPoint.getSignature(), proceedingJoinPoint.getArgs());

        if (AuthUtils.checkToken(annotation, getRequest())) {
            result = (ResponseResult) proceedingJoinPoint.proceed();
        } else {
            result = new ResponseResult();
            result.setCode(CommonConstants.response_code_forbidden);
            result.setMsg("未登录，暂无权限访问！");
        }
        log.info("checkAuth end!");
        return result;
    }

    /**
     * 获取当前请求的request对象
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }
}
