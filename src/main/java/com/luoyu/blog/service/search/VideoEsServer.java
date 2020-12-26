package com.luoyu.blog.service.search;

import com.luoyu.blog.entity.video.vo.VideoVO;

import java.util.List;

public interface VideoEsServer {

    boolean initVideoList() throws Exception;

    List<VideoVO> searchVideoList(String keyword) throws Exception;

}
