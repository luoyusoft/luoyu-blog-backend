package com.luoyu.blog.service.search;

import com.luoyu.blog.entity.search.vo.SearchListVO;

public interface SearchServer {

    SearchListVO searchList(String keyword) throws Exception;

}
