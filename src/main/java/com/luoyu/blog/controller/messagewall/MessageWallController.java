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
import com.luoyu.blog.entity.messagewall.vo.MessageWallListVO;
import com.luoyu.blog.service.messagewall.MessageWallService;
import io.netty.util.internal.StringUtil;
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
     * 后台新增留言
     * @param messageWall messageWall
     */
    @PostMapping("/manage/messagewall")
    @RequiresPermissions("messagewall:add")
    public Response manageAddMessageWall(@RequestBody MessageWall messageWall){
        messageWallService.manageAddMessageWall(messageWall);
        return Response.success();
    }

    /**
     * 后台分页获取留言列表
     */
    @GetMapping("/manage/messagewalls")
    @RequiresPermissions("messagewall:list")
    public Response manageGetMessageWalls(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                          @RequestParam("name") String name, @RequestParam("floorNum") Integer floorNum){
        PageUtils messageWallPage = messageWallService.manageGetMessageWalls(page, limit, name, floorNum);
        return Response.success(messageWallPage);
    }

    /**
     * 后台批量删除
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
     * @param messageWall messageWall
     */
    @PostMapping("/messagewall")
    public Response addMessageWall(@RequestBody MessageWall messageWall){
        ValidatorUtils.validateEntity(messageWall, AddGroup.class);
        messageWallService.addMessageWall(messageWall);
        if (!StringUtils.isEmpty(messageWall.getEmail())){
            if(!FormatUtils.checkEmail(messageWall.getEmail())){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "邮箱格式不对");
            }
        }
        if (!StringUtils.isEmpty(messageWall.getWebsite())){
            if(!messageWall.getWebsite().startsWith("https://") && !messageWall.getWebsite().startsWith("http://")){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "网址格式不对");
            }
        }

        return Response.success();
    }

    /**
     * 按楼层分页获取留言列表
     * @param page page
     * @param limit limit
     * @return MessageWallListVO
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
