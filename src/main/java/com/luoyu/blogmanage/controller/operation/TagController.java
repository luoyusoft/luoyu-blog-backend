package com.luoyu.blogmanage.controller.operation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luoyu.blogmanage.common.constants.RedisCacheNames;
import com.luoyu.blogmanage.common.enums.ModuleEnum;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.entity.base.AbstractController;
import com.luoyu.blogmanage.entity.base.Result;
import com.luoyu.blogmanage.entity.operation.Tag;
import com.luoyu.blogmanage.entity.operation.TagLink;
import com.luoyu.blogmanage.mapper.operation.TagLinkMapper;
import com.luoyu.blogmanage.service.operation.TagService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 标签 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2019-01-21
 */
@RestController
@CacheConfig(cacheNames = RedisCacheNames.TAG)
@RequestMapping("/admin/operation/tag")
public class TagController extends AbstractController {

    @Resource
    private TagService tagService;

    @Resource
    private TagLinkMapper tagLinkMapper;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("operation:tag:list")
    public Result list(@RequestParam("t") Long t, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("key") String key){
        PageUtils tagPage = tagService.queryPage(page, limit, key);
        return Result.ok().put("page", tagPage);
    }

    @GetMapping("/select")
    @RequiresPermissions("operation:tag:list")
    public Result select(@RequestParam("type") Integer type){
        List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().lambda().eq(type != null,Tag::getType,type));
        return Result.ok().put("tagList",tagList);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("operation:tag:info")
    public Result info(@PathVariable("id") String id){
       Tag tag = tagService.getById(id);

        return Result.ok().put("tag", tag);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("operation:tag:save")
    @CacheEvict(allEntries = true)
    public Result save(@RequestBody Tag tag){
        ValidatorUtils.validateEntity(tag);
        tagService.save(tag);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("operation:tag:update")
    @CacheEvict(allEntries = true)
    public Result update(@RequestBody Tag tag){
        ValidatorUtils.validateEntity(tag);
        tagService.updateById(tag);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("operation:tag:delete")
    @CacheEvict(allEntries = true)
    public Result delete(@RequestBody String[] ids){
        for (String id : ids) {
            List<TagLink> tagLinkList = tagLinkMapper.selectList(new QueryWrapper<TagLink>().lambda().eq(TagLink::getTagId, id));
            if(!CollectionUtils.isEmpty(tagLinkList)) {
                TagLink tagLink = tagLinkList.get(0);
                if (tagLink.getType().equals(ModuleEnum.ARTICLE.getValue())) {
                    return Result.error("该标签下有文章，无法删除");
                }
                if (tagLink.getType().equals(ModuleEnum.BOOK.getValue())) {
                    return Result.error("该标签下有图书，无法删除");
                }
                if(tagLink.getType().equals(ModuleEnum.BOOK_NOTE.getValue())) {
                    return Result.error("该标签下有笔记，无法删除");
                }
            }
        }
        tagService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
