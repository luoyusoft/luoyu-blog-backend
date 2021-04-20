package com.luoyu.blog.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.enums.CategoryRankEnum;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.entity.operation.Category;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.operation.CategoryMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.cache.CacheServer;
import com.luoyu.blog.service.operation.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2018-12-17
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Autowired
    private CacheServer cacheServer;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 树状列表
     * @param module
     * @return
     */
    @Override
    public List<Category> select(Integer module) {
        List<Category> categoryList = this.list(new QueryWrapper<Category>().lambda().eq(module!=null,Category::getModule,module));

        //添加顶级分类
        Category root = new Category();
        root.setId(-1);
        root.setName("根目录");
        root.setParentId(-1);
        categoryList.add(root);
        return categoryList;
    }

    /**
     * 信息
     * @param id
     * @return
     */
    @Override
    public Category info(Integer id) {
        return this.getById(id);
    }

    /**
     * 保存
     * @param category
     * @return
     */
    @Override
    public void add(Category category) {
        verifyCategory(category);
        this.save(category);

        cleanCategorysAllCache();
    }

    /**
     * 修改
     * @param category
     * @return
     */
    @Override
    public void update(Category category) {
        this.updateById(category);

        cleanCategorysAllCache();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public void delete(Integer id) {
        //判断是否有子菜单或按钮
        List<Category> categoryList = queryListParentId(id);
        if(categoryList.size() > 0){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "请先删除子级别");
        }
        // 判断是否有文章
        if(articleMapper.checkByCategory(id) > 0) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该类别下有文章，无法删除");
        }
        // 判断是否有视频
        if(videoMapper.checkByCategory(id) > 0) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该类别下有视频，无法删除");
        }

        this.removeById(id);

        cleanCategorysAllCache();
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
            Category parentCategory = info(category.getParentId());
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
     * 查询所有菜单
     *
     * @param name
     * @param module
     * @return
     */
    @Override
    public List<Category> queryWithParentName(String name, Integer module) {
        return baseMapper.queryAll(name, module);
    }

    /**
     * 根据父级别查询子级别
     *
     * @param id
     * @return
     */
    @Override
    public List<Category> queryListParentId(Integer id) {
        return baseMapper.selectList(new QueryWrapper<Category>().lambda()
                .eq(Category::getParentId,id));
    }

    /**
     * 根据类别Id数组查询类别数组
     * @param categoryIds
     * @param categoryList
     * @return
     */
    @Override
    public String renderCategoryArr(String categoryIds, List<Category> categoryList) {
        if (StringUtils.isEmpty(categoryIds)) {
            return "";
        }
        List<String> categoryStrList = new ArrayList<>();
        String[] categoryIdArr = categoryIds.split(",");
        for (int i = 0; i < categoryIdArr.length; i++) {
            Integer categoryId = Integer.parseInt(categoryIdArr[i]);
            // 根据Id查找类别名称
            String categoryStr = categoryList
                    .stream()
                    .filter(category -> category.getId().equals(categoryId))
                    .map(Category::getName)
                    .findAny().orElse("类别已被删除");
            categoryStrList.add(categoryStr);
        }
        return String.join(",",categoryStrList);
    }

    /**
     * 清除缓存
     */
    private void cleanCategorysAllCache(){
        taskExecutor.execute(() ->{
            cacheServer.cleanCategorysAllCache();
        });
    }

    /********************** portal ********************************/

    /**
     * 获取categoryList
     *
     * @param module
     * @return
     */
    @Cacheable(value = RedisKeyConstants.CATEGORYS, key = "#module")
    @Override
    public List<Category> getCategoryList(String module) {
        return baseMapper.selectList(new QueryWrapper<Category>().lambda()
                .eq(!StringUtils.isEmpty(module),Category::getModule,module));
    }

}
