package com.luoyu.blog.project.service.portal.operation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.operation.Recommend;
import com.luoyu.blog.common.entity.operation.vo.RecommendVO;

import java.util.List;

/**
 * RecommendService
 *
 * @author bobbi
 * @date 2019/02/22 13:41
 * @email 571002217@qq.com
 * @description
 */
public interface RecommendService extends IService<Recommend> {

    List<RecommendVO> listRecommendVo();

    List<RecommendVO> listHotRead();
}
