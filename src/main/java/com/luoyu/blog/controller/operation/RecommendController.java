package com.luoyu.blog.controller.operation;

import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.operation.vo.RecommendVO;
import com.luoyu.blog.service.operation.RecommendService;
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
 * 推荐 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
@RestController
@CacheConfig(cacheNames = RedisCacheNames.RECOMMEND)
public class RecommendController extends AbstractController {

    @Resource
    private RecommendService recommendService;

    /**
     * 列表
     */
    @GetMapping("/manage/operation/recommend/list")
    @RequiresPermissions("operation:recommend:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit){
        PageUtils recommendPage = recommendService.queryPage(page, limit);
        return Response.success(recommendPage);
    }

    /**
     * 查找
     */
    @GetMapping("/manage/operation/recommend/select")
    @RequiresPermissions("operation:recommend:list")
    public Response select(@RequestParam("type") Integer type, @RequestParam("title") String title) {
        if(type == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "type不能为空");
        }
        List<RecommendVO> recommendList = recommendService.select(type, title);
        return Response.success(recommendList);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/operation/recommend/info/{id}")
    @RequiresPermissions("operation:recommend:info")
    public Response info(@PathVariable("id") String id){
       Recommend recommend = recommendService.getById(id);
        return Response.success(recommend);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/operation/recommend/save")
    @RequiresPermissions("operation:recommend:save")
    @CacheEvict(allEntries = true)
    public Response save(@RequestBody Recommend recommend){
        if(recommend.getLinkId() == null || recommend.getType() == null || recommend.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "linkId，type，orderNum不能为空");
        }
        ValidatorUtils.validateEntity(recommend, AddGroup.class);
        recommendService.insertRecommend(recommend);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/operation/recommend/update")
    @RequiresPermissions("operation:recommend:update")
    @CacheEvict(allEntries = true)
    public Response update(@RequestBody Recommend recommend){
        if(recommend.getId() == null || recommend.getLinkId() == null
                || recommend.getType() == null || recommend.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id，linkId，type，orderNum不能为空");
        }
        recommendService.updateRecommend(recommend);

        return Response.success();
    }

    /**
     * 推荐置顶
     */
    @PutMapping("/manage/operation/recommend/top/{id}")
    @RequiresPermissions("operation:recommend:update")
    @CacheEvict(allEntries = true)
    public Response updateTop(@PathVariable("id") Integer id){
        if(id == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id不能为空");
        }
        recommendService.updateRecommendTop(id);

        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/operation/recommend/delete")
    @RequiresPermissions("operation:recommend:delete")
    @CacheEvict(allEntries = true)
    public Response deleteRecommendsByIds(@RequestBody Integer[] ids){
        recommendService.deleteRecommendsByIds(Arrays.asList(ids));
        return Response.success();
    }

    /********************** portal ********************************/

    @RequestMapping("/operation/recommends")
    @Cacheable(key = "'RECOMMEND'")
    public Response listRecommend() {
        List<RecommendVO> recommendList = recommendService.listRecommendVO();
        return Response.success(recommendList);
    }

}
