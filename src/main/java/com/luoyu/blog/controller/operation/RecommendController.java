package com.luoyu.blog.controller.operation;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.operation.vo.HomeRecommendInfoVO;
import com.luoyu.blog.entity.operation.vo.RecommendVO;
import com.luoyu.blog.service.operation.RecommendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
public class RecommendController extends AbstractController {

    @Resource
    private RecommendService recommendService;

    /**
     * 获取首页信息
     */
    @GetMapping("/manage/operation/recommend/homeinfo")
    public Response getHomeRecommendInfoVO() {
        HomeRecommendInfoVO homeRecommendInfoVO = recommendService.getHomeRecommendInfoVO();
        return Response.success(homeRecommendInfoVO);
    }

    /**
     * 列表
     * @param page 页码
     * @param limit 每页数量
     * @return 推荐列表
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
    public Response select(@RequestParam("module") Integer module, @RequestParam("title") String title) {
        if(module == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "module不能为空");
        }
        List<RecommendVO> recommendList = recommendService.select(module, title);
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
    public Response save(@RequestBody Recommend recommend){
        if(recommend.getLinkId() == null || recommend.getModule() == null || recommend.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "linkId，module，orderNum不能为空");
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
    public Response update(@RequestBody Recommend recommend){
        if(recommend.getId() == null || recommend.getLinkId() == null
                || recommend.getModule() == null || recommend.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id，linkId，module，orderNum不能为空");
        }
        recommendService.updateRecommend(recommend);

        return Response.success();
    }

    /**
     * 推荐置顶
     */
    @PutMapping("/manage/operation/recommend/top/{id}")
    @RequiresPermissions("operation:recommend:update")
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
    public Response deleteRecommendsByIds(@RequestBody Integer[] ids){
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        recommendService.deleteRecommendsByIds(Arrays.asList(ids));
        return Response.success();
    }

    /********************** portal ********************************/

    @RequestMapping("/operation/recommends")
    public Response listRecommend(@RequestParam("module") Integer module) {
        if (module == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "module不能为空");
        }

        List<RecommendVO> recommendList = recommendService.listRecommendVO(module);
        return Response.success(recommendList);
    }

}
