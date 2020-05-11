package com.jinhaoxun.blog.project.tt.service.quartzservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhaoxun.blog.project.tt.pojo.quartz.Task;
import com.jinhaoxun.blog.common.response.ResponseResult;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-30
 * @description 任务服务接口
 */
public interface ITaskService extends IService<Task> {

    /**
     * @author jinhaoxun
     * @description 获取数据库Simple任务列表
     * @return ResponseResult Simple任务列表
     * @throws Exception
     */
    ResponseResult getTaskList();

    /**
     * @author jinhaoxun
     * @description 修改状态为已执行
     * @param taskId 任务ID
     * @return ResponseResult 修改数据条数
     * @throws Exception
     */
    ResponseResult updateExecutionStatus(Long taskId);

}
