package com.jinhx.blog.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhx.blog.common.constants.RedisKeyConstants;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.util.Query;
import com.jinhx.blog.entity.operation.FriendLink;
import com.jinhx.blog.service.operation.FriendLinkService;
import com.jinhx.blog.entity.operation.vo.HomeFriendLinkInfoVO;
import com.jinhx.blog.mapper.operation.FriendLinkMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
@CacheConfig(cacheNames = RedisKeyConstants.FRIENDLINKS)
@Service
@Slf4j
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    /**
     * 获取首页信息
     * @return 首页信息
     */
    @Override
    public HomeFriendLinkInfoVO getHommeFriendLinkInfoVO() {
        HomeFriendLinkInfoVO homeFriendLinkInfoVO = new HomeFriendLinkInfoVO();
        homeFriendLinkInfoVO.setCount(baseMapper.selectCount());
        return homeFriendLinkInfoVO;
    }

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param title
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("title", title);
        IPage<FriendLink> linkPage = baseMapper.selectPage(new Query<FriendLink>(params).getPage(),
                new QueryWrapper<FriendLink>().lambda().like(StringUtils.isNotEmpty(title), FriendLink::getTitle,title));
        return new PageUtils(linkPage);
    }

    /**
     * 判断上传文件下是否有友链
     * @param url
     * @return
     */
    @Override
    public boolean checkByFile(String url) {
        return baseMapper.checkByFile(url) > 0;
    }

    /********************** portal ********************************/

    /**
     * 获取link列表
     *
     * @return
     */
    @Cacheable
    @Override
    public List<FriendLink> listFriendLink() {
        return baseMapper.selectList(null);
    }

}
