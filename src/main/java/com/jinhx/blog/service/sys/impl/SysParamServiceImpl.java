package com.jinhx.blog.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.util.Query;
import com.jinhx.blog.entity.sys.SysParam;
import com.jinhx.blog.mapper.sys.SysParamMapper;
import com.jinhx.blog.service.sys.SysParamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2018-12-28
 */
@Service
@Slf4j
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParam> implements SysParamService {

    /**
     * 分页查询
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String menuUrl, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));

        IPage<SysParam> paramPage = baseMapper.selectPage(new Query<SysParam>(params).getPage(),
                new QueryWrapper<SysParam>().lambda()
                        .eq(StringUtils.isNotBlank(menuUrl), SysParam::getMenuUrl,menuUrl)
                        .like(StringUtils.isNotBlank(String.valueOf(type)), SysParam::getType,type));
        return new PageUtils(paramPage);
    }

}
