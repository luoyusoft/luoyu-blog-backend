package com.jinhx.blog.controller.search;

import com.jinhx.blog.common.aop.annotation.LogView;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.entity.search.vo.SearchListVO;
import com.jinhx.blog.service.search.SearchServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SearchController
 *
 * @author luoyu
 * @date 2019/03/13 15:04
 * @description
 */
@RestController
public class SearchController {

    @Autowired
    private SearchServer searchServer;

    /********************** portal ********************************/

    /**
     * 搜索
     * @param keyword
     * @return
     */
    @GetMapping("/search")
    @LogView(module = 3)
    public Response searchList(@RequestParam(value = "keyword", required = false) String keyword) throws Exception {
        SearchListVO searchListVO = searchServer.searchList(keyword);
        return Response.success(searchListVO);
    }

}
