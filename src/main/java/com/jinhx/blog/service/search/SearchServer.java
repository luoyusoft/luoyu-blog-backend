package com.jinhx.blog.service.search;

import com.jinhx.blog.entity.search.vo.SearchListVO;

public interface SearchServer {

    SearchListVO searchList(String keyword) throws Exception;

}
