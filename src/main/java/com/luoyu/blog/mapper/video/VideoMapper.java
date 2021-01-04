package com.luoyu.blog.mapper.video;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyu.blog.entity.video.Video;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
public interface VideoMapper extends BaseMapper<Video> {

    /**
     * 查询列表
     *
     * @param page
     * @param params
     * @return
     */
    List<VideoDTO> listVideoDTO(Page<VideoDTO> page, @Param("params") Map<String, Object> params);

    /**
     * 更新观看记录
     * @param id
     */
    Boolean updateWatchNum(Integer id);

    /**
     * 更新点赞
     * @param id
     */
    Boolean updateLikeNum(Integer id);

    /**
     * 判断类别下是否有文章
     * @param categoryId
     * @return
     */
    int checkByCategory(Integer categoryId);

    /**
     * 查询所有视频列表
     * @return
     */
    List<VideoDTO> selectVideoDTOList();

    /**
     * 查询所有视频列表
     * @return
     */
    List<Video> selectVideoListByTitle(String title);

    /**
     * 更新视频
     * @return
     */
    Boolean updateVideoById(Video video);

    /********************** portal ********************************/

    /**
     * 根据条件查询分页
     * @param page
     * @param params
     * @return
     */
    List<VideoDTO> queryPageCondition(Page<VideoDTO> page, @Param("params") Map<String, Object> params);

    /**
     * 获取简单的对象
     * @param id
     * @return
     */
    VideoDTO getSimpleVideoDTO(Integer id);

    /**
     * 获取热观榜
     * @return
     */
    List<VideoDTO> getHotWatchList();

    /**
     * 查询已发布视频
     * @return
     */
    Video selectVideoById(Integer id);

}
