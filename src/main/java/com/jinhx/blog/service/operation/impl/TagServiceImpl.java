package com.jinhx.blog.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhx.blog.common.constants.ModuleTypeConstants;
import com.jinhx.blog.common.constants.RedisKeyConstants;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.util.Query;
import com.jinhx.blog.entity.operation.Tag;
import com.jinhx.blog.entity.operation.TagLink;
import com.jinhx.blog.entity.operation.vo.TagVO;
import com.jinhx.blog.mapper.operation.TagLinkMapper;
import com.jinhx.blog.mapper.operation.TagMapper;
import com.jinhx.blog.service.cache.CacheServer;
import com.jinhx.blog.service.operation.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2019-01-21
 */
@Service
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagLinkMapper tagLinkMapper;

    @Autowired
    private CacheServer cacheServer;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 分页查询
     *
     * @param page
     * @param limit
     * @param name
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String name, Integer module) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", String.valueOf(limit));
        params.put("page", String.valueOf(page));

        IPage<Tag> tagPage = baseMapper.selectPage(new Query<Tag>(params).getPage(),
                new QueryWrapper<Tag>().lambda()
                        .like(StringUtils.isNotEmpty(name), Tag::getName, name)
                        .eq(module != null, Tag::getModule, module));
        return new PageUtils(tagPage);
    }

    /**
     * 根据关联Id获取列表
     *
     * @param linkId
     * @return
     */
    @Override
    public List<Tag> listByLinkId(Integer linkId, Integer module) {
        return baseMapper.listByLinkId(linkId, module);
    }

    /**
     * 添加所属标签，包含新增标签
     *
     * @param tagList
     * @param linkId
     */
    @Override
    public void saveTagAndNew(List<Tag> tagList, Integer linkId, Integer module) {
        Optional.ofNullable(tagList)
                .ifPresent(tagListValue -> tagListValue.forEach(tag -> {
                    if (tag.getId() == null) {
                        baseMapper.insert(tag);
                    }
                    TagLink tagLink = new TagLink(linkId, tag.getId(), module);
                    tagLinkMapper.insert(tagLink);
                }));

        cleanTagsAllCache(module);
    }

    /**
     * 删除tagLink关联
     *
     * @param linkId
     */
    @Override
    public void deleteTagLink(Integer linkId, Integer module) {
        tagLinkMapper.delete(new QueryWrapper<TagLink>().lambda()
                .eq(linkId != null, TagLink::getLinkId, linkId)
                .eq(module != null, TagLink::getModule, module));

        cleanTagsAllCache(module);
    }

    /**
     * 清除缓存
     */
    private void cleanTagsAllCache(Integer module){
        taskExecutor.execute(() ->{
            cacheServer.cleanTagsAllCache(module);
        });
    }

    /********************** portal ********************************/

    /**
     * 获取标签列表
     * @param module 模块
     * @return 标签列表
     */
    @Cacheable(value = RedisKeyConstants.TAGS, key = "#module")
    @Override
    public List<TagVO> listTags(Integer module) {
        List<TagVO> tagVOList = new ArrayList<>();
        if(ModuleTypeConstants.ARTICLE.equals(module)){
            tagVOList = baseMapper.listTagArticleDTO(module);
            return tagVOList.stream().filter(tagVOListItem -> Integer.parseInt(tagVOListItem.getLinkNum()) > 0).collect(Collectors.toList());
        }
        if(ModuleTypeConstants.VIDEO.equals(module)){
            tagVOList = baseMapper.listTagVideoDTO(module);
            return tagVOList.stream().filter(tagVOListItem -> Integer.parseInt(tagVOListItem.getLinkNum()) > 0).collect(Collectors.toList());
        }
        return tagVOList;
    }

}
