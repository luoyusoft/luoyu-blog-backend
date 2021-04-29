package com.jinhx.blog.service.log;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.entity.log.LogView;
import com.jinhx.blog.entity.log.vo.HomeLogInfoVO;

/**
 * LogViewService
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
public interface LogViewService extends IService<LogView> {

    /**
     * 获取首页信息
     * @return 首页信息
     */
    HomeLogInfoVO getHommeLogInfoVO();

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
