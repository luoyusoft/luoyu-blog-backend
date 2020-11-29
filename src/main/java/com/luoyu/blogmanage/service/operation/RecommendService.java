package com.luoyu.blogmanage.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.entity.operation.Recommend;
import com.luoyu.blogmanage.entity.operation.vo.RecommendVO;
import com.luoyu.blogmanage.common.util.PageUtils;
import org.springframework.web.bind.annotation.RequestParam;

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
    List<RecommendVO> listSelect(Integer type, String title);

    /**
     * 更新置顶状态
     * @param id
     */
    void updateTop(Integer id);

    /**
     * 从主删除过来批量删除
     * @param articleIds
     * @param type
     */
    void deleteBatchByLinkIdsAndType(List<Integer> articleIds, int type);

    /**
     * 从主新增过来新增
     * @param articleId
     * @param type
     */
    void insertRecommend(Integer articleId, int type);

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
    void deleteRecommend(List<Integer> ids);

    /**
     * 查找
     * @param linkId
     * @param type
     */
    Recommend selectRecommendByLinkIdAndType(Integer linkId, Integer type);

}
