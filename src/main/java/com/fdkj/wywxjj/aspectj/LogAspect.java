package com.fdkj.wywxjj.aspectj;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.utils.ServletUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志切面
 *
 * @author wyt
 */
//@Aspect
//@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 配置织入点
     */
    @Pointcut("execution(public * com.fdkj.wywxjj.controller.*.*.*(..))")
    public void logPointCut() {
    }

    /**
     * 前置增强
     */
    //@Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        handleBeforeLog(joinPoint);
    }

    /**
     * 后置增强
     */
    //@After("logPointCut()")
    public void doAfter(JoinPoint joinPoint) {
        handleAfterLog(joinPoint);
    }

    /**
     * 返回增强
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doReturning(JoinPoint joinPoint, Object result) {
        handleReturnLog(joinPoint, result);
    }

    /**
     * 异常增强
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void doThrowing(JoinPoint joinPoint, Exception e) {
        handleThrowingLog(joinPoint, e);
    }

    /**
     * 环绕增强
     */
    //@Around("logPointCut()")
    public void doAround(JoinPoint joinPoint) {
        handleAroundLog(joinPoint);
    }

    private void handleBeforeLog(JoinPoint joinPoint) {
    }

    private void handleAfterLog(JoinPoint joinPoint) {
    }

    private void handleReturnLog(JoinPoint joinPoint, Object result) {
        try {
            //请求request
            HttpServletRequest request = ServletUtils.getRequest();
            //请求路径
            String requestURI = request.getRequestURI();
            //请求放
            String requestMethod = request.getMethod();
            log.info("请求参数路劲{}，请求方法{}", requestURI, requestMethod);

            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;

            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //方法名
            String methodName = signature.getName();
            //参数
            Object[] args = joinPoint.getArgs();

            String[] parameterNames = methodSignature.getParameterNames();

            log.info("请求方法: {}.{}", className, methodName);
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                log.info("请求参数: {} = {}", parameterName, args[i]);
            }

            if (result instanceof String) {
                log.info("返回结果: {}", result);
            } else if (result instanceof ResponseEntity) {
                ResponseEntity responseEntity = (ResponseEntity) result;
                CusResponseBody body = (CusResponseBody) responseEntity.getBody();
                log.info("返回结果: {}", JSONObject.toJSONString(body));
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void handleThrowingLog(JoinPoint joinPoint, Exception e) {
        try {
            //请求request
            HttpServletRequest request = ServletUtils.getRequest();
            //请求路径
            String requestURI = request.getRequestURI();
            //请求放
            String requestMethod = request.getMethod();
            log.info("请求参数路劲{}，请求方法{}", requestURI, requestMethod);

            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;

            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //方法名
            String methodName = signature.getName();
            //参数
            Object[] args = joinPoint.getArgs();

            String[] parameterNames = methodSignature.getParameterNames();

            log.info("请求方法: {}.{}", className, methodName);
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                log.info("请求参数: {} = {}", parameterName, args[i]);
            }

            log.error("异常: ", e.getMessage());
        } catch (Exception err) {
            //err.printStackTrace();
        }
    }

    private void handleAroundLog(JoinPoint joinPoint) {
    }
}
