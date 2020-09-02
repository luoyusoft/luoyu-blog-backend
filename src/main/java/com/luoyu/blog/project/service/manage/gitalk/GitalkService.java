package com.luoyu.blog.project.service.manage.gitalk;

import com.luoyu.blog.common.entity.gitalk.InitGitalkRequest;

public interface GitalkService {

    /**
     * @param initGitalkRequest
     * @return
     * @throws Exception
     */
    boolean initArticle(InitGitalkRequest initGitalkRequest) throws Exception ;

    /**
     * @return
     */
    boolean initArticleList();

}
