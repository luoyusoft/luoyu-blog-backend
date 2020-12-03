package com.luoyu.blogmanage.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blogmanage.entity.operation.Recommend;

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
