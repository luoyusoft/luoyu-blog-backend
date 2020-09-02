package com.luoyu.blog.project.service.portal.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.entity.operation.Category;
import com.luoyu.blog.project.service.portal.operation.CategoryService;
import com.luoyu.blog.project.mapper.manage.operation.CategoryMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * CategoryServiceImpl
 *
 * @author bobbi
 * @date 2019/02/19 15:33
 * @email 571002217@qq.com
 * @description
 */
@Service("categoryPortalService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    /**
     * 获取categoryList
     *
     * @param params
     * @return
     */
    @Override
    public List<Category> listCategory(Map<String, Object> params) {
        String type = (String) params.get("type");
        String rank = (String) params.get("rank");
        return baseMapper.selectList(new QueryWrapper<Category>().lambda()
                .eq(StringUtils.isNotEmpty(type),Category::getType,type)
                .eq(StringUtils.isNotEmpty(rank),Category::getRank,rank));
    }
}
