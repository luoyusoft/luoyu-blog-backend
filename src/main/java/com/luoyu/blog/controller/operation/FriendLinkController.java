package com.luoyu.blog.controller.operation;

import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.operation.FriendLink;
import com.luoyu.blog.entity.operation.vo.HomeFriendLinkInfoVO;
import com.luoyu.blog.service.operation.FriendLinkService;
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
 * 友链 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
@RestController
@CacheConfig(cacheNames = RedisKeyConstants.FRIENDLINKS)
public class FriendLinkController extends AbstractController {

    @Resource
    private FriendLinkService friendLinkService;

    /**
     * 获取首页信息
     */
    @GetMapping("/manage/operation/friendlink/homeinfo")
    public Response getHommeFriendLinkInfoVO() {
        HomeFriendLinkInfoVO homeFriendLinkInfoVO = friendLinkService.getHommeFriendLinkInfoVO();
        return Response.success(homeFriendLinkInfoVO);
    }

    /**
     * 列表
     */
    @GetMapping("/manage/operation/friendlink/list")
    @RequiresPermissions("operation:friendlink:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title){
        PageUtils linkPage = friendLinkService.queryPage(page, limit, title);
        return Response.success(linkPage);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/operation/friendlink/info/{id}")
    @RequiresPermissions("operation:friendlink:info")
    public Response info(@PathVariable("id") String id){
       FriendLink friendLink = friendLinkService.getById(id);
        return Response.success(friendLink);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/operation/friendlink/save")
    @RequiresPermissions("operation:friendlink:save")
    @CacheEvict(allEntries = true)
    public Response save(@RequestBody FriendLink friendLink){
        ValidatorUtils.validateEntity(friendLink, AddGroup.class);
        friendLinkService.save(friendLink);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/operation/friendlink/update")
    @RequiresPermissions("operation:friendlink:update")
    @CacheEvict(allEntries = true)
    public Response update(@RequestBody FriendLink friendLink){
        friendLinkService.updateById(friendLink);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/operation/friendlink/delete")
    @RequiresPermissions("operation:friendlink:delete")
    @CacheEvict(allEntries = true)
    public Response delete(@RequestBody String[] ids){
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        friendLinkService.removeByIds(Arrays.asList(ids));
        return Response.success();
    }

    /********************** portal ********************************/

    @RequestMapping("/operation/friendlinks")
    @Cacheable
    public Response listFriendLink() {
        List<FriendLink> friendLinkList = friendLinkService.listFriendLink();
        return Response.success(friendLinkList);
    }

}
