package com.jinhx.blog.service.messagewall.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.util.Query;
import com.jinhx.blog.entity.messagewall.vo.MessageWallListVO;
import com.jinhx.blog.entity.messagewall.vo.MessageWallVO;
import com.jinhx.blog.entity.messagewall.MessageWall;
import com.jinhx.blog.entity.messagewall.vo.HomeMessageWallInfoVO;
import com.jinhx.blog.mapper.messagewall.MessageWallMapper;
import com.jinhx.blog.service.messagewall.MessageWallService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * MessageWallServiceImpl
 *
 * @author luoyu
 * @date 2018/11/21 12:48
 * @description
 */
@Service
public class MessageWallServiceImpl extends ServiceImpl<MessageWallMapper, MessageWall> implements MessageWallService {

    @Value("${message.wall.default.profile}")
    private String messageWallDefaultProfile;

    @Value("${message.wall.default.manage.profile}")
    private String messageWallDefaultManageProfile;

    @Value("${message.wall.default.manage.name}")
    private String messageWallDefaultManageName;

    @Value("${message.wall.default.manage.email}")
    private String messageWallDefaultManageEmail;

    @Value("${message.wall.default.manage.website}")
    private String messageWallDefaultManageWebsite;

    /**
     * 后台获取首页信息
     * @return 首页信息
     */
    @Override
    public HomeMessageWallInfoVO manageGetHomeMessageWallInfoVO() {
        // 当天零点
        LocalDateTime createTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        HomeMessageWallInfoVO homeMessageWallInfoVO = new HomeMessageWallInfoVO();
        homeMessageWallInfoVO.setMaxFloorNum(baseMapper.selectMaxFloorNum());
        homeMessageWallInfoVO.setAllCount(baseMapper.selectMessageWallCount());
        homeMessageWallInfoVO.setTodayCount(baseMapper.selectTodayCount(createTime));
        return homeMessageWallInfoVO;
    }

    /**
     * 后台新增留言
     * @param messageWall 留言
     */
    @Override
    public void manageAddMessageWall(MessageWall messageWall) {
        // 新楼层
        if (MessageWall.REPLY_ID_LAYER_MASTER.equals(messageWall.getReplyId()) || messageWall.getReplyId() == null){
            messageWall.setReplyId(MessageWall.REPLY_ID_LAYER_MASTER);
            messageWall.setFloorNum(baseMapper.selectMaxFloorNum() + 1);
        } else {
            if (messageWall.getFloorNum() == null){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "floorNum不能为空");
            }
        }
        messageWall.setProfile(messageWallDefaultManageProfile);
        messageWall.setName(messageWallDefaultManageName);
        messageWall.setEmail(messageWallDefaultManageEmail);
        messageWall.setWebsite(messageWallDefaultManageWebsite);

        baseMapper.insert(messageWall);
    }

    /**
     * 后台分页查询留言列表
     * @param page 页码
     * @param limit 页数
     * @param name 昵称
     * @param floorNum 楼层数
     * @return 留言列表
     */
    @Override
    public PageUtils manageGetMessageWalls(Integer page, Integer limit, String name, Integer floorNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("name", name);
        params.put("floorNum", floorNum);

        Page<MessageWallVO> messageWallVOPage = new Query<MessageWallVO>(params).getPage();
        List<MessageWallVO> messageWallVOList = baseMapper.selectMessageWallVOs(messageWallVOPage, params);
        messageWallVOPage.setRecords(messageWallVOList);
        return new PageUtils(messageWallVOPage);
    }

    /**
     * 后台批量删除
     * @param ids ids
     */
    @Override
    public void manageDeleteMessageWalls(Integer[] ids) {
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /********************** portal ********************************/

    /**
     * 新增留言
     * @param messageWall 留言
     */
    @Override
    public void addMessageWall(MessageWall messageWall) {
        // 新楼层
        if (MessageWall.REPLY_ID_LAYER_MASTER.equals(messageWall.getReplyId()) || messageWall.getReplyId() == null){
            messageWall.setReplyId(MessageWall.REPLY_ID_LAYER_MASTER);
            messageWall.setFloorNum(baseMapper.selectMaxFloorNum() + 1);
        }else {
            if (messageWall.getFloorNum() == null){
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "floorNum不能为空");
            }
        }
        messageWall.setProfile(messageWallDefaultProfile);

        baseMapper.insert(messageWall);
    }

    /**
     * 按楼层分页获取留言列表
     * @param page 页码
     * @param limit 页数
     * @return 留言列表
     */
    @Override
    public MessageWallListVO getMessageWallListByFloor(Integer page, Integer limit) {
        MessageWallListVO messageWallListVO = new MessageWallListVO();
        messageWallListVO.setTotalCount(baseMapper.selectMessageWallCount());
        Integer maxFloorNum = baseMapper.selectMaxFloorNum() - (page - 1) * limit;
        Integer minFloorNum = maxFloorNum - limit + 1;
        messageWallListVO.setHaveMoreFloor(minFloorNum > 1);
        List<MessageWallVO> messageWallVOS = baseMapper.selectMessageWallVOListByFloor(minFloorNum, maxFloorNum);
        if (CollectionUtils.isEmpty(messageWallVOS)){
            messageWallVOS = new ArrayList<>();
        }
        messageWallListVO.setMessageWallVOList(messageWallVOS);
        return messageWallListVO;
    }

}
