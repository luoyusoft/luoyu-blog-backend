package com.luoyu.blog.service.gitalk;

import com.luoyu.blog.entity.gitalk.InitGitalkRequest;

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
