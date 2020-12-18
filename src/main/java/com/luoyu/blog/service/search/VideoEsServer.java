package com.luoyu.blog.service.search;

import com.luoyu.blog.entity.video.dto.VideoDTO;

import java.util.List;

public interface VideoEsServer {

    boolean initVideoList() throws Exception;

    List<VideoDTO> searchVideoList(String keyword) throws Exception;

}
