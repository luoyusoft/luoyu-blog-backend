package com.luoyu.blogmanage.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.entity.operation.Category;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luoyu
 * @since 2018-12-17
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询所有菜单
     * @param name
     * @param type
     * @return
     */
    List<Category> queryWithParentName(String name, Integer type);

    /**
     * 根据父级别查询子级别
     * @param id
     * @return
     */
    List<Category> queryListParentId(Integer id);

    /**
     * 根据类别Id数组查询类别数组
     * @param categoryIds
     * @param categoryList
     * @return
     */
    String renderCategoryArr(String categoryIds, List<Category> categoryList);

}
