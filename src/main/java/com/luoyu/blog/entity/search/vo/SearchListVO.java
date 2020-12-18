package com.luoyu.blog.entity.search.vo;

import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.video.dto.VideoDTO;
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

    private List<VideoDTO> videoList;

    private List<ArticleDTO> articleList;

}
