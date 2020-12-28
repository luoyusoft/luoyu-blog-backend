package com.luoyu.blog.common.aop.aspect;

import com.luoyu.blog.common.aop.annotation.LogLike;
import com.luoyu.blog.common.api.IPApi;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.HttpContextUtils;
import com.luoyu.blog.common.util.IPUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.UserAgentUtils;
import com.luoyu.blog.entity.sys.IPInfo;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.log.LogLikeMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

import javax.annotation.Resource;
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
public class LogLikeAspect {

    private static final String PROFILES_ACTIVE_PRO = "prod";

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Autowired
    private IPApi ipApi;

    @Autowired
    private LogLikeMapper logLikeMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Pointcut("@annotation(com.luoyu.blog.common.aop.annotation.LogLike)")
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

            com.luoyu.blog.entity.log.LogLike logLikeEntity = new com.luoyu.blog.entity.log.LogLike();
            //获取request
            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

            logLikeEntity.setBorderName(UserAgentUtils.getBorderName(request));
            logLikeEntity.setBorderVersion(UserAgentUtils.getBrowserVersion(request));
            logLikeEntity.setDeviceManufacturer(UserAgentUtils.getDeviceManufacturer(request));
            logLikeEntity.setDeviceType(UserAgentUtils.getDeviceType(request));
            logLikeEntity.setOsVersion(UserAgentUtils.getOsVersion(request));

            //设置IP地址
            logLikeEntity.setIp(IPUtils.getIpAddr(request));
            taskExecutor.execute(() ->{
                //保存日志
                this.saveLogLike(logLikeEntity, point, stopWatch.getTime());
            });

            return result;
        }

        return point.proceed();
    }

    private void saveLogLike(com.luoyu.blog.entity.log.LogLike logLikeEntity, ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogLike logLike = method.getAnnotation(LogLike.class);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        String id = JsonUtils.objectToJson(args[0]);
        // 根据注解类型增加数量
        //注解上的类型
        int type = logLike.type();
        logLikeEntity.setType("");
        if (type == ModuleEnum.ARTICLE.getCode()){
            logLikeEntity.setType(ModuleEnum.ARTICLE.getName());
            articleMapper.updateLikeNum(Integer.parseInt(id));
        }
        if (type == ModuleEnum.VIDEO.getCode()){
            logLikeEntity.setType(ModuleEnum.VIDEO.getName());
            videoMapper.updateLikeNum(Integer.parseInt(id));
        }

        logLikeEntity.setParams(id);

        //设置IP地址信息
        if (!StringUtils.isEmpty(logLikeEntity.getIp())){
            try {
                IPInfo ipInfo = ipApi.getIpInfo(logLikeEntity.getIp());
                if (ipInfo != null){
                    logLikeEntity.setCountry(ipInfo.getCountry());
                    logLikeEntity.setRegion(ipInfo.getRegionName());
                    logLikeEntity.setCity(ipInfo.getCity());
                }
            }catch (Exception e){
                log.error("请求查询ip信息接口失败：{}", e.getMessage());
            }
        }

        logLikeEntity.setTime(time);
        LocalDateTime now = LocalDateTime.now();
        logLikeEntity.setCreateTime(now);
        logLikeEntity.setUpdateTime(now);
        logLikeMapper.insert(logLikeEntity);
    }

}
