package com.jinhaoxun.acweb;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableAsync
@ComponentScan(basePackages = {"com.jinhaoxun.accommon","com.jinhaoxun.acredis", "com.jinhaoxun.acnotify",
        "com.jinhaoxun.acweb","com.jinhaoxun.acservice","com.jinhaoxun.acdao"})
@MapperScan({"com.jinhaoxun.acdao.applymapper","com.jinhaoxun.acdao.shiromapper","com.jinhaoxun.acdao.quartzmapper"})
@EnableScheduling
@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BlogApplication {
    public static void main(String[] args) {
        log.info("========================迈叽叽歪歪，担猛猛开始！========================");
        SpringApplication.run(BlogApplication.class, args);
        log.info("=========================兴死有神秀，无BUG哩！========================");
    }
}