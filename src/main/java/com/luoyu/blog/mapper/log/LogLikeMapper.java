package com.luoyu.blog.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.log.LogLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 阅读日志 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2019-02-15
 */
public interface LogLikeMapper extends BaseMapper<LogLike> {

    /*
     * 查询最大的id
     */
    Integer selectMaxId();

    /*
     * 更新
     */
    Boolean updateLogLikeById(LogLike logLike);

    /**
     * 分页查询
     * @param start
     * @param end
     * @return
     */
    List<LogLike> selectLogLikesByPage(@Param("start") int start, @Param("end") int end);

}
