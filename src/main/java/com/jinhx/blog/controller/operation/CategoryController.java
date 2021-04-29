package com.jinhx.blog.controller.operation;

import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.validator.ValidatorUtils;
import com.jinhx.blog.common.validator.group.AddGroup;
import com.jinhx.blog.entity.base.AbstractController;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.entity.operation.Category;
import com.jinhx.blog.service.operation.CategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-12-17
 */
@RestController
public class CategoryController extends AbstractController {

    @Resource
    private CategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/manage/operation/category/list")
    @RequiresPermissions("operation:category:list")
    public Response list(@RequestParam("name") String name, @RequestParam("module") Integer module){
        List<Category> categoryList = categoryService.queryWithParentName(name, module);
        return Response.success(categoryList);
    }

    /**
     * 树状列表
     */
    @GetMapping("/manage/operation/category/select")
    @RequiresPermissions("operation:category:list")
    public Response select(@RequestParam("module") Integer module){
        List<Category> categoryList = categoryService.select(module);
        return Response.success(categoryList);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/operation/category/info/{id}")
    @RequiresPermissions("operation:category:info")
    public Response info(@PathVariable("id") Integer id){
        Category category = categoryService.info(id);
        return Response.success(category);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/operation/category/save")
    @RequiresPermissions("operation:category:save")
    public Response save(@RequestBody Category category){
        // 数据校验
        ValidatorUtils.validateEntity(category, AddGroup.class);
        categoryService.add(category);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/operation/category/update")
    @RequiresPermissions("operation:category:update")
    public Response update(@RequestBody Category category){
        categoryService.update(category);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/operation/category/delete/{id}")
    @RequiresPermissions("operation:category:delete")
    public Response delete(@PathVariable("id") Integer id){
        categoryService.delete(id);
        return Response.success();
    }

    /********************** portal ********************************/

    @GetMapping("/operation/categories")
    public Response getCategoryList(@RequestParam("module") String module) {
        if (module == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "module不能为空");
        }
        List<Category> categoryList = categoryService.getCategoryList(module);
        return Response.success(categoryList);
    }

}
