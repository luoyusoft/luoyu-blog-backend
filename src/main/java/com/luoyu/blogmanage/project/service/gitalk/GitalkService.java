package com.luoyu.blogmanage.project.service.gitalk;

import com.luoyu.blogmanage.common.entity.gitalk.InitGitalkRequest;

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
