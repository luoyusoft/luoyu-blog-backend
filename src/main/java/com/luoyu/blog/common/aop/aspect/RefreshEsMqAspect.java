package com.luoyu.blog.common.aop.aspect;

import com.luoyu.blog.common.aop.annotation.RefreshEsMqSender;
import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.util.RabbitMqUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * RefreshEsMqAspect
 *
 * @author luoyu
 * @date 2019/03/16 22:53
 * @description
 */
@Aspect
@Component
public class RefreshEsMqAspect {

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    @Pointcut("@annotation(com.luoyu.blog.common.aop.annotation.RefreshEsMqSender)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //执行方法
        Object result = point.proceed();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RefreshEsMqSender senderAnnotation = method.getAnnotation(RefreshEsMqSender.class);
        // 发送刷新信息
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE,
                RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY_INIT,
                senderAnnotation.id() + "," + senderAnnotation.content() + "," + senderAnnotation.operation());
        return result;
    }

}
