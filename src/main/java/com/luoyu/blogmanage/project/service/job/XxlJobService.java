package com.luoyu.blogmanage.project.service.job;

import com.xxl.job.core.biz.model.ReturnT;

/**
 * XxlJob开发示例（Bean模式）
 *
 * 开发步骤：
 * 1、在Spring Bean实例中，开发Job方法，方式格式要求为 "public ReturnT<String> execute(String param)"
 * 2、为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author luoyu 2019-12-11 21:52:51
 */
public interface XxlJobService {

    /**
     * 测试job
     */
    ReturnT<String> testJobHandler(String param) throws Exception;

    /**
     * 初始化es文章数据job
     */
    ReturnT<String> initESArticleJobHandler(String param) throws Exception;

    /**
     * 初始化gitalk文章数据job
     */
    ReturnT<String> initGitalkArticleJobHandler(String param) throws Exception;

    /**
     * 2、分片广播任务
     */
    ReturnT<String> shardingJobHandler(String param) throws Exception;

    /**
     * 3、命令行任务
     */
    ReturnT<String> commandJobHandler(String param) throws Exception;

    /**
     * 4、跨平台Http任务
     */
    ReturnT<String> httpJobHandler(String param) throws Exception;

    /**
     * 5、生命周期任务示例：任务初始化与销毁时，支持自定义相关逻辑；
     */
    ReturnT<String> demoJobHandler2(String param) throws Exception;

    void init();

    void destroy();

}
