package com.luoyu.blog.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.operation.dto.RecommendDTO;

import java.util.List;

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
     * @param page
     * @param limit
     * @param title
     * @return
     */
     PageUtils queryPage(Integer page, Integer limit, String title);

    /**
     * 获取推荐列表
     * @return
     */
    List<RecommendDTO> select(Integer type, String title);

    /**
     * 批量删除
     * @param articleIds
     * @param type
     */
    void deleteRecommendsByLinkIdsAndType(List<Integer> articleIds, int type);

    /**
     * 新增
     * @param recommend
     */
    void insertRecommend(Recommend recommend);

    /**
     * 更新
     * @param recommend
     */
    void updateRecommend(Recommend recommend);

    /**
     * 删除
     * @param ids
     */
    void deleteRecommendsByIds(List<Integer> ids);

    /**
     * 查找
     * @param linkId
     * @param type
     */
    Recommend selectRecommendByLinkIdAndType(Integer linkId, Integer type);

    /********************** portal ********************************/

    List<RecommendDTO> listRecommendDTO();

}
