package com.luoyu.blog.controller.sys;

import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.sys.SysParam;
import com.luoyu.blog.service.sys.SysParamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统参数 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-12-28
 */
@RestController
@Slf4j
public class SysParamController extends AbstractController {

    @Autowired
    private SysParamService paramService;

    /**
     * 列表
     */
    @GetMapping("/manage/sys/param/list")
    @RequiresPermissions("sys:param:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("menuUrl") String menuUrl, @RequestParam("type") String type){
        PageUtils paramPage = paramService.queryPage(page, limit, menuUrl, type);
        return Response.success(paramPage);
    }

    /**
     * 获取所有参数
     */
    @GetMapping("/manage/sys/param/all")
    public Response listAll(){
        List<SysParam> sysParamList = paramService.list(null);
        return Response.success(sysParamList);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/sys/param/info/{id}")
    @RequiresPermissions("sys:param:info")
    public Response info(@PathVariable("id") String id){
       SysParam param = paramService.getById(id);
        return Response.success(param);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/sys/param/save")
    @RequiresPermissions("sys:param:save")
    public Response save(@RequestBody SysParam param){
        ValidatorUtils.validateEntity(param, AddGroup.class);
        paramService.save(param);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/sys/param/update")
    @RequiresPermissions("sys:param:update")
    public Response update(@RequestBody SysParam param){
        ValidatorUtils.validateEntity(param, AddGroup.class);
        paramService.updateById(param);

        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/sys/param/delete")
    @RequiresPermissions("sys:param:delete")
    public Response delete(@RequestBody String[] ids){
        paramService.removeByIds(Arrays.asList(ids));
        return Response.success();
    }

}
