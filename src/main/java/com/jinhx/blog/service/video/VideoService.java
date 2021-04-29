package com.jinhx.blog.service.video;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.entity.video.Video;
import com.jinhx.blog.entity.video.dto.VideoDTO;
import com.jinhx.blog.entity.video.vo.HomeVideoInfoVO;
import com.jinhx.blog.entity.video.vo.VideoVO;

import java.util.List;

/**
 * VideoService
 *
 * @author luoyu
 * @date 2018/11/21 12:47
 * @description
 */
public interface VideoService extends IService<Video> {

    /**
     * 获取首页信息
     * @return 首页信息
     */
    HomeVideoInfoVO getHommeVideoInfoVO();

    /**
     * 分页查询视频列表
     * @param page
     * @param limit
     * @param title
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, String title);

    /**
     * 保存视频
     * @param videoVO
     */
    void saveVideo(VideoVO videoVO);

    /**
     * 批量删除
     * @param ids 文章id数组
     */
    void deleteVideos(Integer[] ids);

    /**
     * 更新视频
     * @param videoVO
     */
    void updateVideo(VideoVO videoVO);

    /**
     * 更新视频状态
     * @param videoVO
     */
    void updateVideoStatus(VideoVO videoVO);

    /**
     * 获取视频对象
     * @param videoId
     * @return
     */
    VideoVO getVideo(Integer videoId);

    /**
     * 判断类别下是否有文章
     * @param categoryId
     * @return
     */
    boolean checkByCategory(Integer categoryId);

    /**
     * 判断上传文件下是否有文章
     * @param url
     * @return
     */
    boolean checkByFile(String url);

    /********************** portal ********************************/

    /**
     * 分页分类获取列表
     * @param page
     * @param limit
     * @param latest
     * @param categoryId
     * @param like
     * @param watch
     * @return
     */
    PageUtils queryPageCondition(Integer page, Integer limit, Boolean latest, Integer categoryId, Boolean like, Boolean watch);

    /**
     * 获取VideoDTO对象
     * @param id
     * @return
     */
    VideoDTO getVideoDTOById(Integer id);

    /**
     * 获取热读榜
     * @return
     */
    List<VideoVO> getHotWatchList();

    /**
     * 更新点赞
     * @return
     */
    Boolean likeVideo(Integer id);

}