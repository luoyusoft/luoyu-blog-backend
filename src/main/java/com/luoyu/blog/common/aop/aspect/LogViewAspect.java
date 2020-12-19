package com.luoyu.blog.common.aop.aspect;

import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.api.IPApi;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.HttpContextUtils;
import com.luoyu.blog.common.util.IPUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.entity.sys.IPInfo;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.log.LogViewMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * ViewLogAspect
 *
 * @author luoyu
 * @date 2019/02/15 14:56
 * @description
 */
@Aspect
@Component
@Slf4j
public class LogViewAspect {

    private static final String PROFILES_ACTIVE_PRO = "prod";

    @Value("spring.profiles.active")
    private String profilesActive;

    @Autowired
    private IPApi ipApi;

    @Autowired
    private LogViewMapper logViewMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Pointcut("@annotation(com.luoyu.blog.common.aop.annotation.LogView)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    @Transactional(rollbackFor = Exception.class)
    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (profilesActive.equals(PROFILES_ACTIVE_PRO)){
            // 耗时计算
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            //执行方法
            Object result = point.proceed();

            stopWatch.stop();
            //保存日志
            this.saveViewLog(point, stopWatch.getTime());

            return result;
        }

        return point.proceed();
    }

    private void saveViewLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.luoyu.blog.entity.log.LogView viewLogEntity = new com.luoyu.blog.entity.log.LogView();
        LogView viewLog = method.getAnnotation(LogView.class);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        String id = JsonUtils.objectToJson(args[0]);
        // 根据注解类型增加数量
        //注解上的类型
        int type = viewLog.type();
        viewLogEntity.setType("");
        if (type == ModuleEnum.ARTICLE.getCode()){
            viewLogEntity.setType(ModuleEnum.ARTICLE.getName());
            articleMapper.updateReadNum(Integer.parseInt(id));
        }
        if (type == ModuleEnum.VIDEO.getCode()){
            viewLogEntity.setType(ModuleEnum.VIDEO.getName());
            videoMapper.updateWatchNum(Integer.parseInt(id));
        }

        viewLogEntity.setParams(id);
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        viewLogEntity.setMethod(className + "." + methodName + "()");
        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        String ip = IPUtils.getIpAddr(request);
        if (ip != null){
            viewLogEntity.setIp(ip);
            IPInfo ipInfo = ipApi.getIpInfo(ip);
            if (ipInfo != null){
                viewLogEntity.setCountry(ipInfo.getCountry());
                viewLogEntity.setRegion(ipInfo.getRegionName());
                viewLogEntity.setCity(ipInfo.getCity());
            }
        }
        viewLogEntity.setTime(time);
        viewLogEntity.setCreateTime(LocalDateTime.now());
        logViewMapper.insert(viewLogEntity);
    }

}
