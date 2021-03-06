package com.jinhx.blog.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.entity.article.Article;
import com.jinhx.blog.entity.article.dto.ArticleDTO;
import com.jinhx.blog.entity.article.vo.ArticleVO;
import com.jinhx.blog.entity.article.vo.HomeArticleInfoVO;

import java.util.List;

/**
 * ArticleService
 *
 * @author luoyu
 * @date 2018/11/21 12:47
 * @description
 */
public interface ArticleService extends IService<Article> {

    /**
     * 获取首页信息
     * @return 首页信息
     */
    HomeArticleInfoVO getHomeArticleInfoVO();

    /**
     * 分页查询文章列表
     * @param page 页码
     * @param limit 页数
     * @param title 标题
     * @return 文章列表
     */
    PageUtils queryPage(Integer page, Integer limit, String title);

    /**
     * 保存文章
     * @param articleVO 文章信息
     */
    void saveArticle(ArticleVO articleVO);

    /**
     * 批量删除
     * @param ids 文章id列表
     */
    void deleteArticles(Integer[] ids);

    /**
     * 更新文章
     * @param articleVO 文章信息
     */
    void updateArticle(ArticleVO articleVO);

    /**
     * 更新文章状态
     * @param articleVO 文章信息
     */
    void updateArticleStatus(ArticleVO articleVO);

    /**
     * 根据文章id获取文章信息
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleVO getArticle(Integer articleId);

    /**
     * 查看未公开文章时检测密码是否正确
     * @param articleId 文章id
     * @param password 密码
     */
    void checkPassword(Integer articleId, String password);

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
     * 分页获取文章列表
     * @param page 页码
     * @param limit 每页数量
     * @param categoryId 分类
     * @param latest 时间排序
     * @param like 点赞量排序
     * @param read 阅读量排序
     * @return 文章列表
     */
    PageUtils listArticles(Integer page, Integer limit, Boolean latest, Integer categoryId, Boolean like, Boolean read);

    /**
     * 分页获取首页文章列表
     * @param page 页码
     * @param limit 每页数量
     * @return 首页文章列表
     */
    PageUtils listHomeArticles(Integer page, Integer limit);

    /**
     * 获取ArticleDTO对象
     * @param id id
     * @param password password
     * @return ArticleDTO
     */
    ArticleDTO getArticleDTO(Integer id, String password);

    /**
     * 获取热读榜
     * @return 热读文章列表
     */
    List<ArticleVO> listHotReadArticles();

    /**
     * 文章点赞
     * @param id id
     * @return 点赞结果
     */
    Boolean updateArticle(Integer id) throws Exception;

    /**
     * 根据文章id获取公开状态
     * @param articleId 文章id
     * @return 公开状态
     */
    Boolean getArticleOpenById(Integer articleId);

}
