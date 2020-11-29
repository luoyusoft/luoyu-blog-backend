package com.luoyu.blogmanage.controller.operation;

import com.luoyu.blogmanage.common.constants.RedisCacheNames;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.entity.base.AbstractController;
import com.luoyu.blogmanage.entity.base.Response;
import com.luoyu.blogmanage.entity.operation.Link;
import com.luoyu.blogmanage.service.operation.LinkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * <p>
 * 友链 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
@RestController
@RequestMapping("/admin/operation/link")
@CacheConfig(cacheNames = RedisCacheNames.LINK)
public class LinkController extends AbstractController {

    @Resource
    private LinkService linkService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("operation:link:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title){
        PageUtils linkPage = linkService.queryPage(page, limit, title);
        return Response.success(linkPage);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("operation:link:info")
    public Response info(@PathVariable("id") String id){
       Link link = linkService.getById(id);
        return Response.success(link);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("operation:link:save")
    @CacheEvict(allEntries = true)
    public Response save(@RequestBody Link link){
        ValidatorUtils.validateEntity(link);
        linkService.save(link);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("operation:link:update")
    @CacheEvict(allEntries = true)
    public Response update(@RequestBody Link link){
        ValidatorUtils.validateEntity(link);
        linkService.updateById(link);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("operation:link:delete")
    @CacheEvict(allEntries = true)
    public Response delete(@RequestBody String[] ids){
        linkService.removeByIds(Arrays.asList(ids));
        return Response.success();
    }

}
