package com.luoyu.blog.common.aop.aspect;

import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.api.IPApi;
import com.luoyu.blog.common.config.params.ParamsHttpServletRequestWrapper;
import com.luoyu.blog.common.util.HttpContextUtils;
import com.luoyu.blog.common.util.IPUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.UserAgentUtils;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Autowired
    private IPApi ipApi;

    @Autowired
    private LogViewMapper logViewMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

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

            com.luoyu.blog.entity.log.LogView viewLogEntity = new com.luoyu.blog.entity.log.LogView();
            viewLogEntity.setResponse(JsonUtils.objectToJson(result));
            //获取request
            ParamsHttpServletRequestWrapper request = HttpContextUtils.getHttpServletRequest();
            //获取request
            viewLogEntity.setBorderName(UserAgentUtils.getBorderName(request));
            viewLogEntity.setBorderVersion(UserAgentUtils.getBrowserVersion(request));
            viewLogEntity.setDeviceManufacturer(UserAgentUtils.getDeviceManufacturer(request));
            viewLogEntity.setDeviceType(UserAgentUtils.getDeviceType(request));
            viewLogEntity.setOsVersion(UserAgentUtils.getOsVersion(request));

            viewLogEntity.setRequestType(request.getMethod());
            viewLogEntity.setUri(request.getRequestURI());
            viewLogEntity.setHeadrParams(request.getQueryString());

            viewLogEntity.setBodyParams(request.getBody());

            //设置IP地址
            viewLogEntity.setIp(IPUtils.getIpAddr(request));
            taskExecutor.execute(() ->{
                //保存日志
                this.saveViewLog(viewLogEntity, point, stopWatch.getTime());
            });

            return result;
        }

        return point.proceed();
    }

    private void saveViewLog(com.luoyu.blog.entity.log.LogView viewLogEntity, ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogView viewLog = method.getAnnotation(LogView.class);

        //注解上的类型
        viewLogEntity.setModule(viewLog.module());

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        viewLogEntity.setMethod(className + "." + methodName + "()");

        //设置IP地址信息
        if (!StringUtils.isEmpty(viewLogEntity.getIp())){
            try {
                IPInfo ipInfo = ipApi.getIpInfo(viewLogEntity.getIp());
                if (ipInfo != null){
                    viewLogEntity.setCountry(ipInfo.getCountry());
                    viewLogEntity.setRegion(ipInfo.getRegionName());
                    viewLogEntity.setCity(ipInfo.getCity());
                }
            }catch (Exception e){
                log.error("请求查询ip信息接口失败：{}", e.getMessage());
            }
        }

        viewLogEntity.setTime(time);
        LocalDateTime now = LocalDateTime.now();
        viewLogEntity.setCreateTime(now);
        viewLogEntity.setUpdateTime(now);
        logViewMapper.insert(viewLogEntity);
    }

}
