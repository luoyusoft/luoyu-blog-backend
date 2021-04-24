package com.luoyu.blog.service.messagewall.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.entity.messagewall.MessageWall;
import com.luoyu.blog.entity.messagewall.vo.MessageWallListVO;
import com.luoyu.blog.entity.messagewall.vo.MessageWallVO;
import com.luoyu.blog.mapper.messagewall.MessageWallMapper;
import com.luoyu.blog.service.messagewall.MessageWallService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * 后台新增留言
     * @param messageWall messageWall
     */
    @Override
    public void manageAddMessageWall(MessageWall messageWall) {
        // 新楼层
        if (messageWall.getReplyId() == null){
            messageWall.setReplyId(MessageWall.REPLY_ID_LAYER_MASTER);
            messageWall.setFloorNum(baseMapper.selectMaxFloorNum() + 1);
        }
        messageWall.setProfile(messageWallDefaultManageProfile);
        messageWall.setName(messageWallDefaultManageName);
        messageWall.setEmail(messageWallDefaultManageEmail);
        messageWall.setWebsite(messageWallDefaultManageWebsite);

        baseMapper.insert(messageWall);
    }

    /**
     * 分页查询文章列表
     *
     * @param page page
     * @param limit limit
     * @param name name
     * @return MessageWallListVO
     */
    @Override
    public MessageWallListVO manageGetMessageWallList(Integer page, Integer limit, String name) {
        MessageWallListVO messageWallListVO = new MessageWallListVO();
        messageWallListVO.setTotalCount(baseMapper.selectMessageWallCount());
        Integer max = page * limit;
        Integer min = max - limit + 1;
        List<MessageWallVO> messageWallVOS = baseMapper.selectMessageWallVOList(min, max, name);
        if (CollectionUtils.isEmpty(messageWallVOS)){
            messageWallVOS = new ArrayList<>();
        }
        messageWallListVO.setMessageWallVOList(messageWallVOS);
        return messageWallListVO;
    }

    /********************** portal ********************************/

    /**
     * 新增留言
     * @param messageWall messageWall
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
     * @param page page
     * @param limit limit
     * @return MessageWallListVO
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
