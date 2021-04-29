package com.jinhx.blog.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.entity.operation.Category;

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
     * 树状列表
     * @param module
     * @return
     */
    List<Category> select(Integer module);

    /**
     * 信息
     * @param id
     * @return
     */
    Category info(Integer id);

    /**
     * 保存
     * @param category
     * @return
     */
    void add(Category category);

    /**
     * 修改
     * @param category
     * @return
     */
    void update(Category category);

    /**
     * 删除
     * @param id
     * @return
     */
    void delete(Integer id);

    /**
     * 查询所有菜单
     * @param name
     * @param module
     * @return
     */
    List<Category> queryWithParentName(String name, Integer module);

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

    /********************** portal ********************************/

    /**
     * 获取categoryList
     * @param module
     * @return
     */
    List<Category> getCategoryList(String module);

}
