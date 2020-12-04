package com.luoyu.blog.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.sys.SysParam;

/**
 * <p>
 * 系统参数 服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-12-28
 */
public interface SysParamService extends IService<SysParam> {

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param menuUrl
     * @param type
     * @return
     */
     PageUtils queryPage(Integer page, Integer limit, String menuUrl, String type);

}
