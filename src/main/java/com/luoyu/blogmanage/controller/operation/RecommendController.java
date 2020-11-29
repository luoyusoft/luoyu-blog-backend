package com.luoyu.blogmanage.controller.operation;

import com.luoyu.blogmanage.common.constants.RedisCacheNames;
import com.luoyu.blogmanage.common.enums.ResponseEnums;
import com.luoyu.blogmanage.common.exception.MyException;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.entity.base.AbstractController;
import com.luoyu.blogmanage.entity.base.Response;
import com.luoyu.blogmanage.entity.operation.Recommend;
import com.luoyu.blogmanage.entity.operation.vo.RecommendVO;
import com.luoyu.blogmanage.service.operation.RecommendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 推荐 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
@RestController
@RequestMapping("/admin/operation/recommend")
@CacheConfig(cacheNames = RedisCacheNames.RECOMMEND)
public class RecommendController extends AbstractController {

    @Resource
    private RecommendService recommendService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("operation:recommend:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title){
        PageUtils recommendPage = recommendService.queryPage(page, limit, title);
        return Response.success(recommendPage);
    }

    /**
     * 查找
     */
    @GetMapping("/select")
    @RequiresPermissions("operation:recommend:list")
    public Response select (@RequestParam("type") Integer type, @RequestParam("title") String title) {
        if(type == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "type不能为空");
        }
        List<RecommendVO> recommendList = recommendService.listSelect(type, title);
        return Response.success(recommendList);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("operation:recommend:info")
    public Response info(@PathVariable("id") String id){
       Recommend recommend = recommendService.getById(id);
        return Response.success(recommend);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("operation:recommend:save")
    @CacheEvict(allEntries = true)
    public Response save(@RequestBody Recommend recommend){
        if(recommend.getLinkId() == null || recommend.getType() == null || recommend.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "linkId，type，orderNum不能为空");
        }
        ValidatorUtils.validateEntity(recommend);
        recommendService.insertRecommend(recommend);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("operation:recommend:update")
    @CacheEvict(allEntries = true)
    public Response update(@RequestBody Recommend recommend){
        if(recommend.getId() == null || recommend.getLinkId() == null
                || recommend.getType() == null || recommend.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id，linkId，type，orderNum不能为空");
        }

        ValidatorUtils.validateEntity(recommend);
        recommendService.updateRecommend(recommend);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/top/{id}")
    @RequiresPermissions("operation:recommend:update")
    @CacheEvict(allEntries = true)
    public Response updateTop (@PathVariable("id") Integer id) {
        recommendService.updateTop(id);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("operation:recommend:delete")
    @CacheEvict(allEntries = true)
    public Response delete(@RequestBody Integer[] ids){
        recommendService.deleteRecommend(Arrays.asList(ids));
        return Response.success();
    }

}
