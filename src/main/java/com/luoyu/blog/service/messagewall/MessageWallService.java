package com.luoyu.blog.service.messagewall;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.messagewall.MessageWall;
import com.luoyu.blog.entity.messagewall.vo.HomeMessageWallInfoVO;
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
     * 后台获取首页信息
     * @return 首页信息
     */
    HomeMessageWallInfoVO manageGetHomeMessageWallInfoVO();

    /**
     * 后台新增留言
     * @param messageWall 留言
     */
    void manageAddMessageWall(MessageWall messageWall);

    /**
     * 后台分页查询留言列表
     * @param page 页码
     * @param limit 页数
     * @param name 昵称
     * @param floorNum 楼层数
     * @return 留言列表
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
     * @param messageWall 留言
     */
    void addMessageWall(MessageWall messageWall);

    /**
     * 按楼层分页获取留言列表
     * @param page 页码
     * @param limit 页数
     * @return 留言列表
     */
    MessageWallListVO getMessageWallListByFloor(Integer page, Integer limit);

}
