package com.luoyu.blog.project.mapper.quartzmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.project.pojo.quartz.Task;

import java.util.List;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-30
 * @description 任务Mapper接口
 */
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * @author jinhaoxun
     * @description 获取数据库Simple任务列表
     * @return ResponseResult Simple任务列表
     */
    List<Task> getTaskList();

    /**
     * @author jinhaoxun
     * @description 修改状态为已执行
     * @param taskId 任务ID
     * @return ResponseResult 修改数据条数
     */
    int updateExecutionStatus(Long taskId);

}
