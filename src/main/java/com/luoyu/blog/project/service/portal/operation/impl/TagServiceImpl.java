package com.luoyu.blog.project.service.portal.operation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.entity.operation.Tag;
import com.luoyu.blog.common.entity.operation.vo.TagVO;
import com.luoyu.blog.project.service.portal.operation.TagService;
import com.luoyu.blog.project.mapper.manage.operation.TagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TagServiceImpl
 *
 * @author bobbi
 * @date 2019/02/22 16:34
 * @email 571002217@qq.com
 * @description
 */
@Service("TagPortalService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    /**
     * 获取tagVoList
     *
     * @return
     */
    @Override
    public List<TagVO> listTagVo() {
        return baseMapper.listTagVo();
    }
}
