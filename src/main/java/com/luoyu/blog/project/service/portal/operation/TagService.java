package com.luoyu.blog.project.service.portal.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.operation.Tag;
import com.luoyu.blog.common.entity.operation.vo.TagVO;

import java.util.List;

/**
 * TagService
 *
 * @author bobbi
 * @date 2019/02/22 16:34
 * @email 571002217@qq.com
 * @description
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取tagVoList
     * @return
     */
    List<TagVO> listTagVo();
}
