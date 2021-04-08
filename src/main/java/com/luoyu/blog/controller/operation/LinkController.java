package com.luoyu.blog.controller.operation;

import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.Link;
import com.luoyu.blog.service.operation.LinkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 友链 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
@RestController
@CacheConfig(cacheNames = RedisCacheNames.LINK)
public class LinkController extends AbstractController {

    @Resource
    private LinkService linkService;

    /**
     * 列表
     */
    @GetMapping("/manage/operation/link/list")
    @RequiresPermissions("operation:link:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title){
        PageUtils linkPage = linkService.queryPage(page, limit, title);
        return Response.success(linkPage);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/operation/link/info/{id}")
    @RequiresPermissions("operation:link:info")
    public Response info(@PathVariable("id") String id){
       Link link = linkService.getById(id);
        return Response.success(link);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/operation/link/save")
    @RequiresPermissions("operation:link:save")
    @CacheEvict(allEntries = true)
    public Response save(@RequestBody Link link){
        ValidatorUtils.validateEntity(link, AddGroup.class);
        linkService.save(link);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/operation/link/update")
    @RequiresPermissions("operation:link:update")
    @CacheEvict(allEntries = true)
    public Response update(@RequestBody Link link){
        linkService.updateById(link);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/operation/link/delete")
    @RequiresPermissions("operation:link:delete")
    @CacheEvict(allEntries = true)
    public Response delete(@RequestBody String[] ids){
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        linkService.removeByIds(Arrays.asList(ids));
        return Response.success();
    }

    /********************** portal ********************************/

    @RequestMapping("/operation/links")
    @Cacheable
    public Response listLink() {
        List<Link> linkList = linkService.listLink();
        return Response.success(linkList);
    }

}
