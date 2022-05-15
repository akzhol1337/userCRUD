package com.example.usercrud.business.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("@annotation(com.example.usercrud.business.entity.annotations.Loggable)")
    public void methodLevel(){

    }

    @Pointcut("within(@com.example.usercrud.business.entity.annotations.Loggable *)")
    public void beanAnnotatedWithLoggable(){

    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {

    }

    @Pointcut("publicMethod() && beanAnnotatedWithLoggable()")
    public void publicMethodInsideAClassAnnotatedWithLoggable(){

    }

    @Before("methodLevel() || publicMethodInsideAClassAnnotatedWithLoggable()")
    public void beforeMethodCall(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        AtomicReference<String> args = new AtomicReference<>("");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> args.updateAndGet(v -> v + arg));
        log.info("Class name: {}, method name: {}, arguments: {}", className, methodName, args.get());
    }

    @AfterReturning(pointcut = "methodLevel() || publicMethodInsideAClassAnnotatedWithLoggable()", returning = "returnValue")
    public void afterMethodCall(JoinPoint joinPoint, Object returnValue) {
        log.info("Returning value: {}", returnValue);
    }

}
