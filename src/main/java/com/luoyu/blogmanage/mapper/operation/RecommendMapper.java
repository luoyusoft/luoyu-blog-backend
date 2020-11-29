package com.luoyu.blogmanage.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blogmanage.entity.operation.Recommend;
import com.luoyu.blogmanage.entity.operation.vo.RecommendVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 推荐 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
public interface RecommendMapper extends BaseMapper<Recommend> {

    /**
     * 获取推荐列表
     * @return
     */
    List<RecommendVO> listRecommendVo();

    /**
     * 获取最热列表
     * @return
     */
    List<RecommendVO> listHotRead();

    /**
     * 获取总数量
     * @return
     */
    int selectCount();

    /**
     * 获取
     * @return
     */
    Recommend selectRecommendByLinkIdAndType(Integer linkId, Integer type);

    /**
     * 更新
     * @return
     */
    Boolean updateRecommendByLinkIdAndType(Recommend recommend);

}
