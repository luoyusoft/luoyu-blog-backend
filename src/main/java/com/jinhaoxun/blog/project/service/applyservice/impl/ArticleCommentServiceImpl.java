package com.jinhaoxun.blog.project.tt.service.applyservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhaoxun.blog.project.tt.pojo.apply.ArticleComment;
import com.jinhaoxun.blog.project.tt.dao.applymapper.ArticleCommentMapper;
import com.jinhaoxun.blog.project.tt.service.applyservice.IArticleCommentService;
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
