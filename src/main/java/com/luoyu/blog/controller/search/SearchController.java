package com.luoyu.blog.controller.search;

import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.search.vo.SearchListVO;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.service.search.ArticleEsServer;
import com.luoyu.blog.service.search.VideoEsServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ArticleEsController
 *
 * @author luoyu
 * @date 2019/03/13 15:04
 * @description
 */
@RestController
public class SearchController {

    @Resource
    private ArticleEsServer articleEsServer;

    @Resource
    private VideoEsServer videoEsServer;

    /********************** portal ********************************/

    /**
     * 搜索标题，描述，内容
     * @param keyword
     * @return
     */
    @GetMapping("/search")
    public Response searchList(@RequestParam(value = "keyword", required = false) String keyword) throws Exception {
        List<ArticleDTO> articleDTOList = articleEsServer.searchArticleList(keyword);
        List<VideoDTO> videoDTOList = videoEsServer.searchVideoList(keyword);
        SearchListVO searchListVO = new SearchListVO();
        searchListVO.setVideoList(videoDTOList);
        searchListVO.setArticleList(articleDTOList);
        return Response.success(searchListVO);
    }

}
