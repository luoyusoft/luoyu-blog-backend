package com.luoyu.blog.project.service.manage.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.entity.sys.SysParam;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.project.service.manage.sys.SysParamService;
import com.luoyu.blog.project.mapper.manage.sys.SysParamMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author bobbi
 * @since 2018-12-28
 */
@Service
@Slf4j
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParam> implements SysParamService {

    /**
     * 分页查询
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String menuUrl = (String) params.get("menuUrl");
        String type = (String) params.get("type");
        IPage<SysParam> page=baseMapper.selectPage(new Query<SysParam>(params).getPage(),
                new QueryWrapper<SysParam>().lambda()
                        .eq(StringUtils.isNotBlank(menuUrl), SysParam::getMenuUrl,menuUrl)
                        .like(StringUtils.isNotBlank(type), SysParam::getType,type));
        return new PageUtils(page);
    }

}
