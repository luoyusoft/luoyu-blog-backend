package com.luoyu.blog.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.log.LogView;
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
public interface LogViewMapper extends BaseMapper<LogView> {

    /*
     * 查询最大的id
     */
    Integer selectMaxId();

    /*
     * 更新
     */
    Boolean updateLogViewById(LogView logView);

    /**
     * 分页查询
     * @param start
     * @param end
     * @return
     */
    List<LogView> selectLogViewsByPage(@Param("start") int start, @Param("end") int end);

}
