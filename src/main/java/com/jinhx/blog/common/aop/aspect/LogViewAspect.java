package com.jinhx.blog.common.aop.aspect;

import com.jinhx.blog.common.aop.annotation.LogView;
import com.jinhx.blog.common.api.IPApi;
import com.jinhx.blog.common.config.params.ParamsHttpServletRequestWrapper;
import com.jinhx.blog.common.util.HttpContextUtils;
import com.jinhx.blog.common.util.IPUtils;
import com.jinhx.blog.common.util.JsonUtils;
import com.jinhx.blog.common.util.UserAgentUtils;
import com.jinhx.blog.entity.sys.IPInfo;
import com.jinhx.blog.mapper.log.LogViewMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * ViewLog
 * @author luoyu
 * @date 2019/02/15 14:51
 * @description 日志
 */
@Aspect
@Component
@Slf4j
@Order(-1)
public class LogViewAspect {

    private static final String PROFILES_ACTIVE_PRO = "prod";

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Autowired
    private IPApi ipApi;

    @Resource
    private LogViewMapper logViewMapper;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Pointcut("@annotation(com.jinhx.blog.common.aop.annotation.LogView)")
    public void logViewPointCut() {

    }

    @Around("logViewPointCut()")
    @Transactional(rollbackFor = Exception.class)
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (profilesActive.equals(PROFILES_ACTIVE_PRO)){
            // 耗时计算
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            //执行方法
            Object result = proceedingJoinPoint.proceed();

            stopWatch.stop();

            com.jinhx.blog.entity.log.LogView viewLogEntity = new com.jinhx.blog.entity.log.LogView();
            viewLogEntity.setResponse(JsonUtils.objectToJson(result));
            //获取request
            ParamsHttpServletRequestWrapper request = (ParamsHttpServletRequestWrapper) HttpContextUtils.getHttpServletRequest();
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
                saveViewLog(viewLogEntity, proceedingJoinPoint, stopWatch.getTime());
            });

            return result;
        }

        return proceedingJoinPoint.proceed();
    }

    private void saveViewLog(com.jinhx.blog.entity.log.LogView viewLogEntity, ProceedingJoinPoint proceedingJoinPoint, long time) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        LogView viewLog = method.getAnnotation(LogView.class);

        //注解上的类型
        viewLogEntity.setModule(viewLog.module());

        // 请求的方法名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
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
