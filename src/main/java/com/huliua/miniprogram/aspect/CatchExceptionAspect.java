package com.huliua.miniprogram.aspect;

import com.huliua.miniprogram.constant.CommonConstants;
import com.huliua.miniprogram.entity.BusinessException;
import com.huliua.miniprogram.entity.ResponseResult;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 异常捕获切面
 */
@Aspect
@Log4j2
@Component
public class CatchExceptionAspect {

    @Pointcut("@within(com.huliua.miniprogram.annotation.CatchException)")
    public void pointCut() {}

    /**
     * 自定义异常处理
     * 当运行有异常抛出时，捕获异常，并以ResponseResult对象的形式返回异常信息
     */
    @Around("pointCut() || @annotation(com.huliua.miniprogram.annotation.CatchException)")
    public ResponseResult around(ProceedingJoinPoint proceedingJoinPoint) {
        ResponseResult result = null;
        try {
            result = (ResponseResult) proceedingJoinPoint.proceed();
        } catch (BusinessException ex) {
            log.error("occur an business exception: ", ex);
            result = new ResponseResult();
            result.setMsg(ex.getMessage());
            result.setCode(ex.getCode());
        } catch (Exception ex) {
            log.error("occur an exception: ", ex);
            result = new ResponseResult();
            result.setMsg(ex.getMessage());
            result.setCode(CommonConstants.response_code_error);
        } catch (Throwable throwable) {
            log.error("occur an exception: ", throwable);
            result = new ResponseResult();
            result.setMsg(throwable.getMessage());
            result.setCode(CommonConstants.response_code_error);
        }
        return result;
    }
}
