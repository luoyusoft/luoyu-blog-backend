package com.jinhx.blog.service.operation;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.entity.operation.Tag;
import com.jinhx.blog.entity.operation.vo.TagVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author luoyu
 * @since 2019-01-21
 */
public interface TagService extends IService<Tag> {

    /**
     * 分页查询
     *
     * @param page
     * @param limit
     * @param name
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, String name, Integer module);

    /**
     * 根据关联Id获取列表
     *
     * @param linkId
     * @param module
     * @return
     */
    List<Tag> listByLinkId(Integer linkId, Integer module);

    /**
     * 添加所属标签，包含新增标签
     *
     * @param tagList
     * @param linkId
     * @param module
     */
    void saveTagAndNew(List<Tag> tagList, Integer linkId, Integer module);

    /**
     * 删除tagLink关联
     * @param linkId
     * @param module
     */
    void deleteTagLink(Integer linkId, Integer module);

    /********************** portal ********************************/

    /**
     * 获取标签列表
     * @param module 模块
     * @return 标签列表
     */
    List<TagVO> listTags(Integer module);

}
