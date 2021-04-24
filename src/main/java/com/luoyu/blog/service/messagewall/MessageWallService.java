package com.luoyu.blog.service.messagewall;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.messagewall.MessageWall;
import com.luoyu.blog.entity.messagewall.vo.MessageWallListVO;

/**
 * MessageWallService
 *
 * @author luoyu
 * @date 2018/11/21 12:47
 * @description
 */
public interface MessageWallService extends IService<MessageWall> {

    /**
     * 后台新增留言
     * @param messageWall messageWall
     */
    void manageAddMessageWall(MessageWall messageWall);

    /**
     * 后台分页查询留言列表
     * @param page page
     * @param limit limit
     * @param name name
     * @param floorNum floorNum
     * @return PageUtils
     */
    PageUtils manageGetMessageWalls(Integer page, Integer limit, String name, Integer floorNum);

    /**
     * 后台批量删除
     * @param ids ids
     */
    void manageDeleteMessageWalls(Integer[] ids);

    /********************** portal ********************************/

    /**
     * 新增留言
     * @param messageWall messageWall
     */
    void addMessageWall(MessageWall messageWall);

    /**
     * 按楼层分页获取留言列表
     * @param page page
     * @param limit limit
     * @return MessageWallListVO
     */
    MessageWallListVO getMessageWallListByFloor(Integer page, Integer limit);

}
