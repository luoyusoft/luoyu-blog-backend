package com.luoyu.blog.controller.operation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luoyu.blog.common.constants.ModuleTypeConstants;
import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.Tag;
import com.luoyu.blog.entity.operation.TagLink;
import com.luoyu.blog.entity.operation.vo.TagVO;
import com.luoyu.blog.mapper.operation.TagLinkMapper;
import com.luoyu.blog.service.operation.TagService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.Cacheable;
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
public class TagController extends AbstractController {

    @Resource
    private TagService tagService;

    @Resource
    private TagLinkMapper tagLinkMapper;

    /**
     * 列表
     */
    @GetMapping("/manage/operation/tag/list")
    @RequiresPermissions("operation:tag:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("key") String key){
        PageUtils tagPage = tagService.queryPage(page, limit, key);
        return Response.success(tagPage);
    }

    /**
     * 列表
     */
    @GetMapping("/manage/operation/tag/select")
    @RequiresPermissions("operation:tag:list")
    public Response select(@RequestParam("module") Integer module){
        List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().lambda().eq(module != null,Tag::getModule,module));
        return Response.success(tagList);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/operation/tag/info/{id}")
    @RequiresPermissions("operation:tag:info")
    public Response info(@PathVariable("id") String id){
       Tag tag = tagService.getById(id);
        return Response.success(tag);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/operation/tag/save")
    @RequiresPermissions("operation:tag:save")
    public Response save(@RequestBody Tag tag){
        ValidatorUtils.validateEntity(tag, AddGroup.class);
        tagService.save(tag);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/operation/tag/update")
    @RequiresPermissions("operation:tag:update")
    public Response update(@RequestBody Tag tag){
        ValidatorUtils.validateEntity(tag, AddGroup.class);
        tagService.updateById(tag);

        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/operation/tag/delete")
    @RequiresPermissions("operation:tag:delete")
    public Response delete(@RequestBody String[] ids){
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        for (String id : ids) {
            List<TagLink> tagLinkList = tagLinkMapper.selectList(new QueryWrapper<TagLink>().lambda().eq(TagLink::getTagId, id));
            if(!CollectionUtils.isEmpty(tagLinkList)) {
                TagLink tagLink = tagLinkList.get(0);
                if (tagLink.getModule().equals(ModuleTypeConstants.ARTICLE)) {
                    throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该标签下有文章，无法删除");
                }
            }
        }
        tagService.removeByIds(Arrays.asList(ids));

        return Response.success();
    }

    /********************** portal ********************************/

    @GetMapping("/operation/tags")
    @Cacheable(value = RedisKeyConstants.TAGS, key = "#module")
    public Response listTag(@RequestParam("module") Integer module) {
        if (module == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "module不能为空");
        }

        List<TagVO> tagList = tagService.listTagDTO(module);
        return Response.success(tagList);
    }

}
