package com.jinhaoxun.blog.project.tt.service.testservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 测试服务类
 */
@Slf4j
@Service
public class TestService {

    @Async("taskExecutor")
    public void sendMessage1() throws InterruptedException {
        // 发送提醒短信 1
        log.info("发送短信方法---- 1   执行开始");
        // 模拟耗时
        Thread.sleep(5000);
        log.info("发送短信方法---- 1   执行结束");
    }


    @Async("taskExecutor")
    public void sendMessage2() throws InterruptedException {
        // 发送提醒短信 2
        log.info("发送短信方法---- 2   执行开始");
        // 模拟耗时
        Thread.sleep(2000);
        log.info("发送短信方法---- 2   执行结束");
    }
}
