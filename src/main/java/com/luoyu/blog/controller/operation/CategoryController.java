package com.luoyu.blog.controller.operation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.enums.CategoryRankEnum;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.Category;
import com.luoyu.blog.service.article.ArticleService;
import com.luoyu.blog.service.operation.CategoryService;
import com.luoyu.blog.service.video.VideoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = RedisCacheNames.CATEGORY)
public class CategoryController extends AbstractController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleService articleService;

    @Resource
    private VideoService videoService;

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
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(module!=null,Category::getModule,module));

        //添加顶级分类
        Category root = new Category();
        root.setId(-1);
        root.setName("根目录");
        root.setParentId(-1);
        categoryList.add(root);

        return Response.success(categoryList);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/operation/category/info/{id}")
    @RequiresPermissions("operation:category:info")
    public Response info(@PathVariable("id") Integer id){
        Category category = categoryService.getById(id);
        return Response.success(category);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/operation/category/save")
    @RequiresPermissions("operation:category:save")
    @CacheEvict(allEntries = true)
    public Response save(@RequestBody Category category){
        // 数据校验
        ValidatorUtils.validateEntity(category, AddGroup.class);
        verifyCategory(category);
        categoryService.save(category);

        return Response.success();
    }

    /**
     * 数据校验
     * @param category
     */
    private void verifyCategory(Category category) {
        //上级分类级别
        int parentRank = CategoryRankEnum.ROOT.getCode();
        if (category.getParentId() != CategoryRankEnum.FIRST.getCode()
                && category.getParentId() != CategoryRankEnum.ROOT.getCode()) {
            Category parentCategory = categoryService.getById(category.getParentId());
            parentRank = parentCategory.getRank();
        }

        // 一级
        if (category.getRank() == CategoryRankEnum.FIRST.getCode()) {
            if (category.getParentId() != CategoryRankEnum.ROOT.getCode()){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上级目录只能为根目录");
            }
        }

        //二级
        if (category.getRank() == CategoryRankEnum.SECOND.getCode()) {
            if (parentRank != CategoryRankEnum.FIRST.getCode()) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上级目录只能为一级类型");
            }
        }

        //三级
        if (category.getRank() == CategoryRankEnum.THIRD.getCode()) {
            if (parentRank != CategoryRankEnum.SECOND.getCode()) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上级目录只能为二级类型");
            }
        }
    }

    /**
     * 修改
     */
    @PutMapping("/manage/operation/category/update")
    @RequiresPermissions("operation:category:update")
    @CacheEvict(allEntries = true)
    public Response update(@RequestBody Category category){
        categoryService.updateById(category);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/operation/category/delete/{id}")
    @RequiresPermissions("operation:category:delete")
    @CacheEvict(allEntries = true)
    public Response delete(@PathVariable("id") Integer id){
        //判断是否有子菜单或按钮
        List<Category> categoryList = categoryService.queryListParentId(id);
        if(categoryList.size() > 0){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "请先删除子级别");
        }
        // 判断是否有文章
        if(articleService.checkByCategory(id)) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该类别下有文章，无法删除");
        }
        // 判断是否有视频
        if(videoService.checkByCategory(id)) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该类别下有视频，无法删除");
        }

        categoryService.removeById(id);

        return Response.success();
    }

    /********************** portal ********************************/

    @GetMapping("/operation/categories")
    @Cacheable
    public Response listCategory(@RequestParam("module") String module) {
        if (module == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "module不能为空");
        }
        List<Category> categoryList = categoryService.listCategory(module);
        return Response.success(categoryList);
    }

}
