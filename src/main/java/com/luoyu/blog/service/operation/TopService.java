package com.luoyu.blog.service.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.operation.Top;
import com.luoyu.blog.entity.operation.vo.TopVO;

import java.util.List;

/**
 * <p>
 * 置顶 服务类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
public interface TopService extends IService<Top> {

    /**
     * 分页查询
     * @param page
     * @param limit
     * @return
     */
     PageUtils queryPage(Integer page, Integer limit);

    /**
     * 获取推荐列表
     * @return
     */
    List<TopVO> select(Integer module, String title);

    /**
     * 批量删除
     * @param linkIds
     * @param module
     */
    void deleteTopsByLinkIdsAndType(List<Integer> linkIds, int module);

    /**
     * 新增
     * @param top
     */
    void insertTop(Top top);

    /**
     * 更新
     * @param top
     */
    void updateTop(Top top);

    /**
     * 置顶
     * @param id
     */
    void updateTopTop(Integer id);

    /**
     * 删除
     * @param ids
     */
    void deleteTopsByIds(List<Integer> ids);

    /**
     * 查找
     * @param linkId
     * @param module
     */
    Top selectTopByLinkIdAndType(Integer linkId, Integer module);

    /**
     * 查找最大顺序
     */
    Integer selectTopMaxOrderNum();

    /********************** portal ********************************/

    List<TopVO> listTopVO(Integer module);

}
