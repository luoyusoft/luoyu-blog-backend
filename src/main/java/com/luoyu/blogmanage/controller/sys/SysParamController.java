package com.luoyu.blogmanage.controller.sys;

import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.entity.base.AbstractController;
import com.luoyu.blogmanage.entity.base.Result;
import com.luoyu.blogmanage.entity.sys.SysParam;
import com.luoyu.blogmanage.service.sys.SysParamService;
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
@RequestMapping("/admin/sys/param")
public class SysParamController extends AbstractController {

    @Autowired
    private SysParamService paramService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:param:list")
    public Result list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("menuUrl") String menuUrl, @RequestParam("type") Integer type){
        PageUtils paramPage = paramService.queryPage(page, limit, menuUrl, type);
        return Result.ok().put("page", paramPage);
    }

    /**
     * 获取所有参数
     */
    @GetMapping("/all")
    public Result listAll(){
        List<SysParam> sysParamList = paramService.list(null);
        return Result.ok().put("sysParamList",sysParamList);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:param:info")
    public Result info(@PathVariable("id") String id){
       SysParam param = paramService.getById(id);

        return Result.ok().put("param", param);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:param:save")
    public Result save(@RequestBody SysParam param){
        ValidatorUtils.validateEntity(param);
        paramService.save(param);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("sys:param:update")
    public Result update(@RequestBody SysParam param){
        ValidatorUtils.validateEntity(param);
        paramService.updateById(param);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:param:delete")
    public Result delete(@RequestBody String[] ids){
        paramService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
