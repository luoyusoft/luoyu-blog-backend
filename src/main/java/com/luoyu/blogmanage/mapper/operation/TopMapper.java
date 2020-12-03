package com.luoyu.blogmanage.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blogmanage.entity.operation.Top;

/**
 * <p>
 * 置顶 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
public interface TopMapper extends BaseMapper<Top> {

    /**
     * 获取
     * @return
     */
    Top selectTopByLinkIdAndType(Integer linkId, Integer type);

    /**
     * 更新
     * @return
     */
    Boolean updateTopByLinkIdAndType(Top top);

}
