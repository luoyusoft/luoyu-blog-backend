package com.luoyu.blog.project.service.applyservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.project.service.applyservice.IArticleCommentService;
import com.luoyu.blog.project.pojo.apply.ArticleComment;
import com.luoyu.blog.project.mapper.applymapper.ArticleCommentMapper;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 文章评论服务类
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements IArticleCommentService {

}
