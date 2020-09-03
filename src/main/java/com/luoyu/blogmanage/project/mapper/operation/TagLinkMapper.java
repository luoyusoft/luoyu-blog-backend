package com.luoyu.blogmanage.project.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blogmanage.common.entity.operation.TagLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章标签多对多维护表 Mapper 接口
 * </p>
 *
 * @author bobbi
 * @since 2019-01-07
 */
@Mapper
public interface TagLinkMapper extends BaseMapper<TagLink> {

}
