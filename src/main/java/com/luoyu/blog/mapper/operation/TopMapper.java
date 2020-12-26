package com.luoyu.blog.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.operation.Top;
import com.luoyu.blog.entity.operation.vo.TopVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

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
     * 获取
     * @return
     */
    Top selectTopByOrderNum(Integer orderNum);

    /**
     * 获取列表
     * @return
     */
    List<Top> selectTops();

    /**
     * 批量修改顺序，注意从大的开始，不然会有唯一索引冲突
     * @return
     */
    Integer updateTopsOrderNumById(List<Top> tops);

    /**
     * 更新
     * @return
     */
    Boolean updateTopOrderNumByLinkIdAndType(Top top);

    /**
     * 推荐置顶
     * @param id
     */
    Boolean updateTopOrderNumById(@Param("orderNum") Integer orderNum, @Param("id") Integer id);

    /**
     * 查找最大顺序
     */
    Integer selectTopMaxOrderNum();

    /********************** portal ********************************/

    /**
     * 获取推荐列表
     * @return
     */
    List<TopVO> listTopDTO(Integer type);

}
