package org.rpc.core.spring.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.rpc.core.annotation.RpcAutowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.annotation.Target;
import java.lang.reflect.Method;


/**
 * :@annotation只能用于拦截在方法上定义了对应注解的方法，
 * 在类上的注解@annotation是不起作用的，因为spring aop本身就是基于方法拦截的。
 */
@Aspect
@Component
public class BeanAop {

    @Pointcut("@annotation(org.rpc.core.annotation.RpcAutowired)")
    public void beanPointCut(){
    }

    @Pointcut("execution(public * org.rpc.core.test.Student.say(..))")
    public void beanPointCut2(){
    }

    @Around("beanPointCut()")
    public Object afterBean(ProceedingJoinPoint  joinPoint) throws Throwable {
        System.out.println(joinPoint.getTarget().getClass());
        System.out.println("开始");
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        System.out.println(method);
        return joinPoint.proceed();
    }
    @Before("beanPointCut2()")
    public void afterBean2(JoinPoint joinPoint) throws Throwable {
//        System.out.println(joinPoint.getTarget().getClass());
//        System.out.println("before1");
    }
}
