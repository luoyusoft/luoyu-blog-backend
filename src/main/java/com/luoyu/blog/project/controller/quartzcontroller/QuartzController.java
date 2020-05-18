package com.luoyu.blog.project.controller.quartzcontroller;

import com.luoyu.blog.project.pojo.quartz.AddCronJobReq;
import com.luoyu.blog.project.pojo.quartz.AddSimpleJobReq;
import com.luoyu.blog.project.pojo.quartz.DeleteJobReq;
import com.luoyu.blog.common.response.ResponseResult;
import com.luoyu.blog.project.service.quartzservice.IQuartzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Quartz前端控制器
 */
@RequestMapping("/quartz")
@RestController
@Api("Quartz接口")
public class QuartzController {

    @Resource
    private IQuartzService iQuartzService;

    /**
     * @author jinhaoxun
     * @description 新增Simple任务
     * @param addSimpleJobReq 任务参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @PostMapping(value = "/simplejob", produces = "application/json; charset=UTF-8")
    @ApiOperation("新增Simple任务")
    public ResponseResult addSimpleJob(@Validated @RequestBody AddSimpleJobReq addSimpleJobReq) throws Exception {
        return iQuartzService.addSimpleJob(addSimpleJobReq);
    }

    /**
     * @author jinhaoxun
     * @description 加入数据库Simple任务到任务列表
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @PostMapping(value = "/simplejoblist", produces = "application/json; charset=UTF-8")
    @ApiOperation("加入数据库Simple任务到任务列表")
    public ResponseResult addSimpleJobList() throws Exception {
        return iQuartzService.addSimpleJobList();
    }

    /**
     * @author jinhaoxun
     * @description 新增Cron任务
     * @param addCronJobReq 任务参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @PostMapping(value = "/cronjob", produces = "application/json; charset=UTF-8")
    @ApiOperation("新增Cron任务")
    public ResponseResult addCronJob(@Validated @RequestBody AddCronJobReq addCronJobReq) throws Exception {
        return iQuartzService.addCronJob(addCronJobReq);
    }

    /**
     * @author jinhaoxun
     * @description 删除任务
     * @param deleteJobReq 删除任务参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @DeleteMapping(value = "/job", produces = "application/json; charset=UTF-8")
    @ApiOperation("删除任务")
    public ResponseResult deleteJob(@Validated @RequestBody DeleteJobReq deleteJobReq) throws Exception {
        return iQuartzService.deleteJob(deleteJobReq);
    }

    /**
     * @author jinhaoxun
     * @description 关闭调度器
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @DeleteMapping(value = "/scheduler", produces = "application/json; charset=UTF-8")
    @ApiOperation("关闭调度器")
    public ResponseResult deleteScheduler() throws Exception {
        return iQuartzService.deleteScheduler();
    }
}

