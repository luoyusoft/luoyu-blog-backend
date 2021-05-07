package com.jinhx.blog.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.entity.operation.vo.HomeRecommendInfoVO;
import com.jinhx.blog.entity.operation.Recommend;
import com.jinhx.blog.entity.operation.vo.RecommendVO;

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
     * 获取首页信息
     * @return 首页信息
     */
    HomeRecommendInfoVO getHomeRecommendInfoVO();

    /**
     * 分页查询
     * @param page 页码
     * @param limit 每页数量
     * @return 推荐列表
     */
     PageUtils queryPage(Integer page, Integer limit);

    /**
     * 获取推荐列表
     * @return
     */
    List<RecommendVO> select(Integer module, String title);

    /**
     * 批量删除
     * @param linkIds
     * @param module
     */
    void deleteRecommendsByLinkIdsAndType(List<Integer> linkIds, int module);

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
     * 推荐置顶
     * @param id
     */
    void updateRecommendTop(Integer id);

    /**
     * 删除
     * @param ids
     */
    void deleteRecommendsByIds(List<Integer> ids);

    /**
     * 查找
     * @param linkId
     * @param module
     */
    Recommend selectRecommendByLinkIdAndType(Integer linkId, Integer module);

    /**
     * 查找最大顺序
     */
    Integer selectRecommendMaxOrderNum();

    /********************** portal ********************************/

    /**
     * 获取推荐列表
     * @param module 模块
     * @return 推荐列表
     */
    List<RecommendVO> listRecommends(Integer module);

}
