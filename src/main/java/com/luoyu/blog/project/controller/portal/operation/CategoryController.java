package com.luoyu.blog.project.controller.portal.operation;

import com.luoyu.blog.common.base.Result;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.entity.operation.Category;
import com.luoyu.blog.project.service.portal.operation.CategoryService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * CategoryController
 *
 * @author bobbi
 * @date 2019/02/19 15:28
 * @email 571002217@qq.com
 * @description
 */
@RestController("categoryPortalController")
@CacheConfig(cacheNames = RedisCacheNames.CATEGORY)
@RequestMapping("/operation")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/categories")
    @Cacheable
    public Result listCategory(@RequestParam Map<String, Object> params) {
        List<Category> categoryList = categoryService.listCategory(params);
        return Result.ok().put("categoryList",categoryList);
    }

}
