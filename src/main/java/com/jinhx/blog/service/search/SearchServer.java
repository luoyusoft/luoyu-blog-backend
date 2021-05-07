package com.jinhx.blog.service.search;

import com.jinhx.blog.entity.search.vo.SearchListVO;

public interface SearchServer {

    /**
     * 搜索，包括文章，视频
     * @param keyword 关键字
     * @return 搜索结果，包括文章，视频
     */
    SearchListVO search(String keyword) throws Exception;

}
