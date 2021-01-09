package com.luoyu.blog.service.log;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.log.LogView;

/**
 * LogViewService
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
public interface LogViewService extends IService<LogView> {

    /**
     * 分页查询日志
     * @param page
     * @param limit
     * @param module
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, Integer module);

    /**
     * 清洗城市信息
     * @return
     */
    void cleanCityInfo();

}
