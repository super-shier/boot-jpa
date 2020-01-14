package com.shier.common.boot.jpa.common.fitter;

import com.google.common.util.concurrent.RateLimiter;
import com.shier.common.boot.jpa.common.config.ApiResponse;
import com.shier.common.boot.jpa.common.config.RateLimit;
import com.shier.common.boot.jpa.common.enums.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: liyunbiao
 * @Date: 2019/8/16 4:38 PM
 * @description
 */
@Aspect
@Component
public class RateLimitAspect {
    private final static Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);
    private RateLimiter rateLimiter = RateLimiter.create(Double.MAX_VALUE);

    /**
     * 定义切点
     * 1、通过扫包切入
     * 2、带有指定注解切入
     */
    @Pointcut("@annotation(com.shier.common.boot.jpa.common.config.RateLimit)")
    public void checkPointcut() {
    }

    @Around(value = "checkPointcut()")
    public Object aroundNotice(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("拦截到了{}方法...", pjp.getSignature().getName());
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        //获取目标方法
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(RateLimit.class)) {
            //获取目标方法的@LxRateLimit注解
            RateLimit lxRateLimit = targetMethod.getAnnotation(RateLimit.class);
            rateLimiter.setRate(lxRateLimit.perSecond());
            if (!rateLimiter.tryAcquire(lxRateLimit.timeOut(), lxRateLimit.timeOutUnit()))
                return new ApiResponse(ErrorCode.SYSTEM_ERROR,null);
        }
        return pjp.proceed();
    }
}