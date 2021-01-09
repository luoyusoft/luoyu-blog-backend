package com.luoyu.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.operation.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2018-12-17
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询所有类别
     * @param name
     * @param module
     * @return
     */
    List<Category> queryAll(@Param("name") String name, @Param("module") Integer module);

}
