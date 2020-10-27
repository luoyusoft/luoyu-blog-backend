package com.luoyu.blogmanage.service.gitalk;

import com.luoyu.blogmanage.entity.gitalk.InitGitalkRequest;

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
