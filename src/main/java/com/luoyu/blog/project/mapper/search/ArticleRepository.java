package com.luoyu.blog.project.mapper.search;

import com.luoyu.blog.common.entity.article.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * ArticleRepository
 *
 * @author luoyu
 * @date 2019/03/13 15:00
 * @description
 */
@Component
public interface ArticleRepository extends ElasticsearchRepository<Article, Integer> {
}
