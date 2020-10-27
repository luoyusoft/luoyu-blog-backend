package com.luoyu.blogmanage.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.entity.operation.Recommend;
import com.luoyu.blogmanage.entity.operation.vo.RecommendVO;
import com.luoyu.blogmanage.common.util.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 推荐 服务类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
public interface RecommendService extends IService<Recommend> {

    /**
     * 分页查询
     * @param params
     * @return
     */
     PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取推荐列表
     * @return
     */
    List<RecommendVO> listSelect();

    /**
     * 更新置顶状态
     * @param id
     */
    void updateTop(Integer id);

    /**
     * 批量删除
     * @param articleIds
     * @param value
     */
    void deleteBatchByLinkId(Integer[] articleIds, int value);

    /**
     * 新增
     * @param articleId
     * @param value
     */
    void insertRecommend(Integer articleId, int value);

}
