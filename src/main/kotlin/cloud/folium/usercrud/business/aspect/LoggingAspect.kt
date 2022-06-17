package cloud.folium.usercrud.business.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.atomic.AtomicReference

@Component
@Aspect
class LoggingAspect {
    @Pointcut("@annotation(cloud.folium.usercrud.business.entity.annotations.Loggable)")
    fun methodLevel() {
    }

    @Pointcut("within(@cloud.folium.usercrud.business.entity.annotations.Loggable *)")
    fun beanAnnotatedWithLoggable() {
    }

    @Pointcut("execution(public * *(..))")
    fun publicMethod() {
    }

    @Pointcut("publicMethod() && beanAnnotatedWithLoggable()")
    fun publicMethodInsideAClassAnnotatedWithLoggable() {
    }

    @Before("methodLevel() || publicMethodInsideAClassAnnotatedWithLoggable()")
    fun beforeMethodCall(joinPoint: JoinPoint) {
        val className = joinPoint.target.javaClass.name
        val methodName = joinPoint.signature.name
        val args = AtomicReference("")
        Arrays.stream(joinPoint.args).forEach { arg: Any -> args.updateAndGet { v: String -> v + arg } }
        //log.info("Class name: {}, method name: {}, arguments: {}", className, methodName, args.get())
    }

    @AfterReturning(
        pointcut = "methodLevel() || publicMethodInsideAClassAnnotatedWithLoggable()",
        returning = "returnValue"
    )
    fun afterMethodCall(joinPoint: JoinPoint?, returnValue: Any?) {
        //LoggingAspect.log.info("Returning value: {}", returnValue)
    }
}