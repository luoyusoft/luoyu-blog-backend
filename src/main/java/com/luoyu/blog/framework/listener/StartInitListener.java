package com.luoyu.blog.framework.listener;

import com.luoyu.blog.common.exception.CustomRuntimeException;
import com.luoyu.blog.common.response.ResponseMsg;
import com.luoyu.blog.project.service.quartzservice.IQuartzService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 启动项目初始化数据监听器
 */
@Slf4j
@WebListener
public class StartInitListener implements ServletContextListener {

    @Resource
    private IQuartzService iQuartzService;

    /**
     * @author jinhaoxun
     * @description 上下文初始化时操作
     * @param sce
     * @throws CustomRuntimeException
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("开始初始化任务列表...");
        try {
            iQuartzService.addSimpleJobList();
        } catch (Exception e) {
            throw new CustomRuntimeException(ResponseMsg.QUARTZ_INIT_JOB_LIST_FAIL.getCode(),
                    ResponseMsg.QUARTZ_INIT_JOB_LIST_FAIL.getMsg() + e.getMessage());
        }
        log.info("初始化任务列表成功！");
    }

    /**
     * @author jinhaoxun
     * @description 上下文销毁时操作
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
