package com.luoyu.blog.project.service.manage.gitalk;

import com.luoyu.blog.common.entity.gitalk.InitGitalkRequest;

public interface GitalkServer {

    /**
     * @param initGitalkRequest
     * @return
     * @throws Exception
     */
    boolean init(InitGitalkRequest initGitalkRequest);

}
