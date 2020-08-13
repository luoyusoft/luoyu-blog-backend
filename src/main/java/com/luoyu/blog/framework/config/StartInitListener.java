//package com.luoyu.blog.framework.listener;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.luoyu.blog.common.exception.CustomRuntimeException;
//import com.luoyu.blog.common.response.ResponseMsg;
//import com.luoyu.blog.project.service.quartzservice.IQuartzService;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//import java.io.IOException;
//import java.util.List;
//
///**
// * @version 1.0
// * @author jinhaoxun
// * @date 2019-08-09
// * @description 启动项目初始化数据监听器
// */
//@Slf4j
//@WebListener
//public class StartInitListener implements ServletContextListener {
//
//    @Resource
//    private IQuartzService iQuartzService;
//
//    /**
//     * @author jinhaoxun
//     * @description 上下文初始化时操作
//     * @param sce
//     * @throws CustomRuntimeException
//     */
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        log.info("开始初始化任务列表...");
//        try {
//            iQuartzService.addSimpleJobList();
//        } catch (Exception e) {
//            throw new CustomRuntimeException(ResponseMsg.QUARTZ_INIT_JOB_LIST_FAIL.getCode(),
//                    ResponseMsg.QUARTZ_INIT_JOB_LIST_FAIL.getMsg() + e.getMessage());
//        }
//        log.info("初始化任务列表成功！");
//
//        try {
//            //手动确认消息已经被消费
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            articleRepository.deleteAll();
//            List<Article> list = articleService.list(new QueryWrapper<Article>().lambda().eq(Article::getPublish,true));
//            articleRepository.saveAll(list);
//            log.info(message.toString());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @author jinhaoxun
//     * @description 上下文销毁时操作
//     * @param sce
//     */
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//
//    }
//}
