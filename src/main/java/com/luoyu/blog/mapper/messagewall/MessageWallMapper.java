package com.luoyu.blog.mapper.messagewall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyu.blog.entity.messagewall.MessageWall;
import com.luoyu.blog.entity.messagewall.vo.MessageWallVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 留言墙 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
public interface MessageWallMapper extends BaseMapper<MessageWall> {

    /**
     * 分页获取留言列表
     *
     * @param page page
     * @param params params
     * @return 列表
     */
    List<MessageWallVO> selectMessageWallVOs(Page<MessageWallVO> page, @Param("params") Map<String, Object> params);

    /**
     * 获取最大楼层数
     * @return 最大楼层数
     */
    Integer selectMaxFloorNum();

    /**
     * 获取总留言数
     * @return 总留言数
     */
    Integer selectMessageWallCount();

    /**
     * 是否有更多楼层
     * @return 是否有更多楼层
     */
    Boolean haveMoreFloor(@Param("floorNum") Integer floorNum);

    /**
     * 按楼层分页获取留言列表
     * @param minFloorNum minFloorNum
     * @param maxFloorNum maxFloorNum
     * @return 列表
     */
    List<MessageWallVO> selectMessageWallVOListByFloor(@Param("minFloorNum") Integer minFloorNum, @Param("maxFloorNum") Integer maxFloorNum);

}
