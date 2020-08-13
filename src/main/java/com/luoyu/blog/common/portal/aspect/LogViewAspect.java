package com.luoyu.blog.common.portal.aspect;

import com.luoyu.blog.common.util.HttpContextUtils;
import com.luoyu.blog.common.util.IPUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.portal.annotation.LogView;
import com.luoyu.blog.project.mapper.article.ArticleMapper;
import com.luoyu.blog.project.mapper.book.BookMapper;
import com.luoyu.blog.project.mapper.book.BookNoteMapper;
import com.luoyu.blog.project.mapper.log.LogViewMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * ViewLogAspect
 *
 * @author bobbi
 * @date 2019/02/15 14:56
 * @email 571002217@qq.com
 * @description
 */
@Aspect
@Component
@Slf4j
public class LogViewAspect {

    @Autowired
    private LogViewMapper logViewMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookNoteMapper bookNoteMapper;

    @Pointcut("@annotation(com.luoyu.blog.common.portal.annotation.LogView)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    @Transactional(rollbackFor = Exception.class)
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveViewLog(point, time);

        return result;
    }

    private void saveViewLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.luoyu.blog.common.entity.log.LogView viewLogEntity = new com.luoyu.blog.common.entity.log.LogView();
        LogView viewLog = method.getAnnotation(LogView.class);
        //注解上的类型
        String type = viewLog.type();
        viewLogEntity.setType(type);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        String id = JsonUtils.toJson(args[0]);
        // 根据注解类型增加数量
        switch (type) {
            case "article":
                articleMapper.updateReadNum(Integer.parseInt(id));
                break;
            case "book":
                bookMapper.updateReadNum(Integer.parseInt(id));
                break;
            case "bookNote":
                bookNoteMapper.updateReadNum(Integer.parseInt(id));
                break;
            default:
                break;
        }
        viewLogEntity.setParams(id);
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        viewLogEntity.setMethod(className + "." + methodName + "()");
        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        viewLogEntity.setIp(IPUtils.getIpAddr(request));
        viewLogEntity.setTime(time);
        viewLogEntity.setCreateDate(LocalDateTime.now());
        logViewMapper.insert(viewLogEntity);

    }
}
