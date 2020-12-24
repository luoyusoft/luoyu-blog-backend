package com.luoyu.blog.service.log.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.api.IPApi;
import com.luoyu.blog.entity.log.LogLike;
import com.luoyu.blog.entity.sys.IPInfo;
import com.luoyu.blog.mapper.log.LogLikeMapper;
import com.luoyu.blog.service.log.LogLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LogLikeServiceImpl
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
@Slf4j
@Service
public class LogLikeServiceImpl extends ServiceImpl<LogLikeMapper, LogLike> implements LogLikeService {

    @Autowired
    private IPApi ipApi;

    @Override
    public void cleanCityInfo() {
        log.info("开始清洗log_like表");
        Integer maxId = baseMapper.selectMaxId();
        if (maxId == null || maxId < 1){
            return;
        }
        for (int start = 0, end = 200; start < maxId; start += 200, end += 200) {
            List<LogLike> logLikes = baseMapper.selectLogLikesByPage(start, end);
            logLikes.forEach(logLikesItem -> {
                try {
                    IPInfo ipInfo = ipApi.getIpInfo(logLikesItem.getIp());
                    logLikesItem.setCountry(ipInfo.getCountry());
                    logLikesItem.setRegion(ipInfo.getRegionName());
                    logLikesItem.setCity(ipInfo.getCity());
                    logLikesItem.setUpdateTime(LocalDateTime.now());
                    baseMapper.updateLogLikeById(logLikesItem);
                    log.info("清洗成功：{}", logLikesItem);
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.getStackTrace();
                }
            });
        }
        log.info("清洗log_like表结束");
    }

}
