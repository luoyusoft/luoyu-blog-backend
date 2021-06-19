package com.jinhx.blog.service.search;

import com.jinhx.blog.entity.video.vo.VideoVO;

import java.util.List;

public interface VideoEsServer {

    boolean initVideoList() throws Exception;

    /**
     * 搜索视频
     * @param keyword 关键字
     * @return 搜索结果
     */
    List<VideoVO> searchVideoList(String keyword) throws Exception;

}
