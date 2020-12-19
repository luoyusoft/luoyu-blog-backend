package com.luoyu.blog.service.gitalk;

import com.luoyu.blog.entity.gitalk.InitGitalkRequest;

public interface GitalkService {

    /**
     * @return
     */
    boolean initArticleList();

    /**
     * @return
     */
    boolean initVideoList();

}
