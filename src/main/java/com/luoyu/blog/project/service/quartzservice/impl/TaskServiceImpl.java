package com.luoyu.blog.project.service.quartzservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.project.pojo.quartz.Task;
import com.luoyu.blog.common.response.ResponseFactory;
import com.luoyu.blog.common.response.ResponseResult;
import com.luoyu.blog.project.mapper.quartzmapper.TaskMapper;
import com.luoyu.blog.project.service.quartzservice.ITaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-30
 * @description 任务服务类
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Resource
    private TaskMapper taskMapper;

    /**
     * @author jinhaoxun
     * @description 获取数据库Simple任务列表
     * @return ResponseResult Simple任务列表
     * @throws Exception
     */
    @Override
    public ResponseResult getTaskList(){
        return ResponseFactory.buildSuccessResponse(taskMapper.getTaskList());
    }

    /**
     * @author jinhaoxun
     * @description 修改状态为已执行
     * @param taskId 任务ID
     * @return ResponseResult 修改数据条数
     * @throws Exception
     */
    @Override
    public ResponseResult updateExecutionStatus(Long taskId){
        return ResponseFactory.buildSuccessResponse(taskMapper.updateExecutionStatus(taskId));
    }
}
