package com.luoyu.blog.controller.operation;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.Top;
import com.luoyu.blog.entity.operation.vo.TopVO;
import com.luoyu.blog.service.operation.TopService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 置顶 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
@RestController
public class TopController extends AbstractController {

    @Resource
    private TopService topService;

    /**
     * 列表
     */
    @GetMapping("/manage/operation/top/list")
    @RequiresPermissions("operation:top:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit){
        PageUtils TopPage = topService.queryPage(page, limit);
        return Response.success(TopPage);
    }

    /**
     * 查找
     */
    @GetMapping("/manage/operation/top/select")
    @RequiresPermissions("operation:top:list")
    public Response select(@RequestParam("module") Integer module, @RequestParam("title") String title) {
        if(module == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "module不能为空");
        }
        List<TopVO> TopList = topService.select(module, title);
        return Response.success(TopList);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/operation/top/info/{id}")
    @RequiresPermissions("operation:top:info")
    public Response info(@PathVariable("id") String id){
       Top Top = topService.getById(id);
        return Response.success(Top);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/operation/top/save")
    @RequiresPermissions("operation:top:save")
    public Response save(@RequestBody Top top){
        if(top.getLinkId() == null || top.getModule() == null || top.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "linkId，module，orderNum不能为空");
        }
        ValidatorUtils.validateEntity(top, AddGroup.class);
        topService.insertTop(top);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/operation/top/update")
    @RequiresPermissions("operation:top:update")
    public Response update(@RequestBody Top top){
        if(top.getId() == null || top.getLinkId() == null
                || top.getModule() == null || top.getOrderNum() == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id，linkId，module，orderNum不能为空");
        }
        topService.updateTop(top);

        return Response.success();
    }

    /**
     * 置顶置顶
     */
    @PutMapping("/manage/operation/top/top/{id}")
    @RequiresPermissions("operation:top:update")
    public Response updateTop(@PathVariable("id") Integer id){
        if(id == null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id不能为空");
        }
        topService.updateTopTop(id);

        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/operation/top/delete")
    @RequiresPermissions("operation:top:delete")
    public Response deleteTopsByIds(@RequestBody Integer[] ids){
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        topService.deleteTopsByIds(Arrays.asList(ids));
        return Response.success();
    }

}
