package com.jinhx.blog.common.aop.aspect;

import com.jinhx.blog.common.aop.annotation.RefreshEsMqSender;
import com.jinhx.blog.common.constants.RabbitMQConstants;
import com.jinhx.blog.common.util.RabbitMQUtils;
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
 * @author jinhx
 * @date 2019/03/16 22:52
 * @description RefreshEsMqAspect
 */
@Aspect
@Component
public class RefreshEsMqAspect {

    @Resource
    private RabbitMQUtils rabbitmqUtils;

    @Pointcut("@annotation(com.jinhx.blog.common.aop.annotation.RefreshEsMqSender)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //执行方法
        Object result = proceedingJoinPoint.proceed();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        RefreshEsMqSender senderAnnotation = method.getAnnotation(RefreshEsMqSender.class);
        // 发送刷新信息
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.BLOG_ARTICLE_TOPIC_EXCHANGE,
                RabbitMQConstants.TOPIC_GITALK_INIT_ROUTINGKEY,
                senderAnnotation.id() + "," + senderAnnotation.content() + "," + senderAnnotation.operation());
        return result;
    }

}
