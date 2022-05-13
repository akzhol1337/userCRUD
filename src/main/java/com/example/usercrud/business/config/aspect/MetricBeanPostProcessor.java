package com.example.usercrud.business.config.aspect;

import com.example.usercrud.business.config.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.proxy.InvocationHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class MetricBeanPostProcessor implements BeanPostProcessor {
    private final MeterRegistry meterRegistry;
    Map<String, Boolean> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        for (Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(Metric.class)) {
                map.put(beanName, true);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!map.containsKey(beanName)) {
            return bean;
        }
        Class<?> beanClass = bean.getClass();
        if (beanClass.getInterfaces().length == 0) {
            return Enhancer.create(beanClass, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                    log.info("salam1");
                    if (method.isAnnotationPresent(Metric.class)) {
                        String metricName = method.getAnnotation(Metric.class).name();
                        meterRegistry.counter(metricName).increment();
                    }
                    return method.invoke(bean, args);
                }
            });
        }

        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
            new java.lang.reflect.InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    log.info("salam2");
                    if (method.isAnnotationPresent(Metric.class)) {
                        String metricName = method.getAnnotation(Metric.class).name();
                        meterRegistry.counter(metricName).increment();
                    }
                    return method.invoke(bean, args);

                }
            });
    }
}
