package com.luoyu.blog.service.log.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.entity.log.LogView;
import com.luoyu.blog.entity.sys.SysRole;
import com.luoyu.blog.mapper.log.LogViewMapper;
import com.luoyu.blog.service.log.LogViewService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * LogViewServiceImpl
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
@Service
public class LogViewServiceImpl extends ServiceImpl<LogViewMapper, LogView> implements LogViewService {

    /**
     * 分页查询角色
     * @param page
     * @param limit
     * @param type
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("type", type);

        String module = null;
        if (type != null){
            if (type.equals(ModuleEnum.ARTICLE.getCode())){
                module = ModuleEnum.ARTICLE.getName();
            }
            if (type.equals(ModuleEnum.VIDEO.getCode())){
                module = ModuleEnum.VIDEO.getName();
            }
        }

        IPage<LogView> logViewIPage = baseMapper.selectPage(new Query<LogView>(params).getPage(),
                new QueryWrapper<LogView>().lambda()
                .eq(!StringUtils.isEmpty(module), LogView::getType,module)
                .orderByDesc(LogView::getCreateTime)
        );
        return new PageUtils(logViewIPage);
    }

}
