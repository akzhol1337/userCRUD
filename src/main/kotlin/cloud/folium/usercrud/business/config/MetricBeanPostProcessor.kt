package cloud.folium.usercrud.business.config

import org.springframework.beans.factory.config.BeanPostProcessor
import io.micrometer.core.instrument.MeterRegistry
import kotlin.Throws
import org.springframework.beans.BeansException
import cloud.folium.usercrud.business.entity.annotations.Metric
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.HashMap

class MetricBeanPostProcessor : BeanPostProcessor {

//    @Autowired
//    lateinit var meterRegistry: MeterRegistry
//    var map: MutableMap<String, Class<*>> = HashMap()
//
//    @Throws(BeansException::class)
//    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
//        val beanClass: Class<*> = bean.javaClass
//        for (method in beanClass.methods) {
//            if (method.isAnnotationPresent(Metric::class.java)) {
//                map[beanName] = beanClass
//            }
//        }
//        return bean
//    }
//
//    @Throws(BeansException::class)
//    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
//        val beanClass = map[beanName] ?: return bean
//        return if (beanClass.interfaces.isEmpty()) {
//            Enhancer.create(beanClass, object: InvocationHandler {
//                override fun invoke(obj: Any?, method: Method?, args: Array<out Any>?): Any {
//                    if (method!!.isAnnotationPresent(Metric::class.java)) {
//                        val metricName: String = method.getAnnotation(Metric::class.java).name
//                        meterRegistry.counter(metricName).increment()
//                    }
//                    return method.invoke(bean, args)
//                }
//
//            })
//        } else Proxy.newProxyInstance(beanClass.classLoader, beanClass.interfaces, object: java.lang.reflect.InvocationHandler {
//            override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
//                if (method!!.isAnnotationPresent(Metric::class.java)) {
//                    val metricName: String = method.getAnnotation(Metric::class.java).name
//                    meterRegistry.counter(metricName).increment()
//                }
//                return method.invoke(bean, args)
//            }
//
//        })
//    }

}