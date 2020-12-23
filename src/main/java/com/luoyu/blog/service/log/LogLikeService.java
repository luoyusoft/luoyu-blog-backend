package com.luoyu.blog.service.log;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.entity.log.LogLike;

/**
 * LogViewService
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
public interface LogLikeService extends IService<LogLike> {

    /**
     * 清洗城市信息
     * @return
     */
    void cleanCityInfo();

}
