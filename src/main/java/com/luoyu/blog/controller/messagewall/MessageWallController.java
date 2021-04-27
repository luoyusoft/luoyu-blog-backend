package com.luoyu.blog.controller.messagewall;

import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.FormatUtils;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.messagewall.MessageWall;
import com.luoyu.blog.entity.messagewall.vo.HomeMessageWallInfoVO;
import com.luoyu.blog.entity.messagewall.vo.MessageWallListVO;
import com.luoyu.blog.service.messagewall.MessageWallService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * MessageWallController
 *
 * @author luoyu
 * @date 2018/11/20 20:25
 * @description
 */
@RestController
public class MessageWallController {

    @Resource
    private MessageWallService messageWallService;

    /**
     * 后台获取首页信息
     * @return 首页信息
     */
    @GetMapping("/manage/messagewall/homeinfo")
    @RequiresPermissions("messagewall:list")
    public Response manageGetHomeMessageWallInfoVO() {
        HomeMessageWallInfoVO homeMessageWallInfoVO = messageWallService.manageGetHomeMessageWallInfoVO();
        return Response.success(homeMessageWallInfoVO);
    }

    /**
     * 后台新增留言
     * @param messageWall 留言
     */
    @PostMapping("/manage/messagewall")
    @RequiresPermissions("messagewall:add")
    public Response manageAddMessageWall(@RequestBody MessageWall messageWall){
        messageWallService.manageAddMessageWall(messageWall);
        if (messageWall.getComment().length() > 2000){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "comment长度不能大于2000");
        }

        return Response.success();
    }

    /**
     * 后台分页查询留言列表
     * @param page 页码
     * @param limit 页数
     * @param name 昵称
     * @param floorNum 楼层数
     * @return 留言列表
     */
    @GetMapping("/manage/messagewalls")
    @RequiresPermissions("messagewall:list")
    public Response manageGetMessageWalls(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                          @RequestParam("name") String name, @RequestParam("floorNum") Integer floorNum){
        if (page < 1 || limit < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "page，limit不能小于1");
        }

        PageUtils messageWallPage = messageWallService.manageGetMessageWalls(page, limit, name, floorNum);
        return Response.success(messageWallPage);
    }

    /**
     * 后台批量删除
     * @param ids ids
     */
    @DeleteMapping("/manage/messagewall")
    @RequiresPermissions("messagewall:delete")
    public Response manageDeleteMessageWallList(@RequestBody Integer[] ids) {
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        messageWallService.manageDeleteMessageWalls(ids);
        return Response.success();
    }

    /********************** portal ********************************/

    /**
     * 新增留言
     * @param messageWall 留言
     */
    @PostMapping("/messagewall")
    public Response addMessageWall(@RequestBody MessageWall messageWall){
        ValidatorUtils.validateEntity(messageWall, AddGroup.class);
        if (!StringUtils.isEmpty(messageWall.getEmail())){
            if(!FormatUtils.checkEmail(messageWall.getEmail())){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "邮箱格式不对");
            }
            if (messageWall.getEmail().length() > 50){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "邮箱长度不能大于50");
            }
        }

        if (!StringUtils.isEmpty(messageWall.getWebsite())){
            if(!messageWall.getWebsite().startsWith("https://") && !messageWall.getWebsite().startsWith("http://")){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "网址格式不对");
            }
            if (messageWall.getWebsite().length() > 1000){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "网址长度不能大于1000");
            }
        }

        if (messageWall.getName().length() > 50){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "昵称长度不能大于50");
        }

        if (messageWall.getComment().length() > 2000){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "内容长度不能大于2000");
        }

        messageWallService.addMessageWall(messageWall);

        return Response.success();
    }

    /**
     * 按楼层分页获取留言列表
     * @param page 页码
     * @param limit 页数
     * @return 留言列表
     */
    @GetMapping("/messagewalls")
    @LogView(module = 5)
    public Response getMessageWallListByFloor(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        if (page < 1 || limit < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "page，limit不能小于1");
        }

        MessageWallListVO messageWallListVO = messageWallService.getMessageWallListByFloor(page, limit);
        return Response.success(messageWallListVO);
    }

}
