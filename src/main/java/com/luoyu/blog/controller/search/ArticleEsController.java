package com.luoyu.blog.controller.search;

import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.service.search.ArticleEsServer;
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
public class ArticleEsController {

    @Resource
    private ArticleEsServer articleEsServer;

    /********************** portal ********************************/

    /**
     * 搜索标题，描述，内容
     * @param keyword
     * @return
     */
    @GetMapping("/articles/search")
    public Response searchArticleList(@RequestParam("keyword") String keyword) throws Exception {
        List<ArticleDTO> articleDTOList= articleEsServer.searchArticleList(keyword);
        return Response.success(articleDTOList);
    }

}
