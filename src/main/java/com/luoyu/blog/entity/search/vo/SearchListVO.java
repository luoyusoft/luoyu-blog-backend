package com.luoyu.blog.entity.search.vo;

import com.luoyu.blog.entity.article.vo.ArticleVO;
import com.luoyu.blog.entity.video.vo.VideoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 搜索
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
@Data
public class SearchListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<VideoVO> videoList;

    private List<ArticleVO> articleList;

}
