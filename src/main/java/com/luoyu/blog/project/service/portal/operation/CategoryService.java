package com.luoyu.blog.project.service.portal.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.operation.Category;

import java.util.List;
import java.util.Map;

/**
 * CategoryService
 *
 * @author bobbi
 * @date 2019/02/19 15:33
 * @email 571002217@qq.com
 * @description
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取categoryList
     * @param params
     * @return
     */
    List<Category> listCategory(Map<String, Object> params);
}
