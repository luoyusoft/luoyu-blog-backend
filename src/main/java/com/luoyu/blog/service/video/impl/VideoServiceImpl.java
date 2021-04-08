package com.luoyu.blog.service.video.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constants.GitalkConstants;
import com.luoyu.blog.common.constants.RabbitMQConstants;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.common.util.RabbitMQUtils;
import com.luoyu.blog.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.entity.operation.Category;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.video.Video;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.entity.video.vo.VideoVO;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.operation.CategoryService;
import com.luoyu.blog.service.operation.RecommendService;
import com.luoyu.blog.service.operation.TagService;
import com.luoyu.blog.service.video.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * VideoServiceImpl
 *
 * @author luoyu
 * @date 2018/11/21 12:48
 * @description
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RecommendService recommendService;

    @Resource
    private RabbitMQUtils rabbitmqUtils;

    /**
     * 分页查询文章列表
     *
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

        Page<VideoDTO> videoDTOPage = new Query<VideoDTO>(params).getPage();
        List<VideoDTO> videoDTOList = baseMapper.listVideoDTO(videoDTOPage, params);
        // 查询所有分类
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(Category::getModule,ModuleEnum.VIDEO.getCode()));
        // 封装ArticleVo
        List<VideoVO> videoVOList = new ArrayList<>();
        Optional.ofNullable(videoDTOList).ifPresent((videoDTOs ->
                videoDTOs.forEach(videoDTO -> {
                    // 设置类别
                    videoDTO.setCategoryListStr(categoryService.renderCategoryArr(videoDTO.getCategoryId(), categoryList));
                    // 设置标签列表
                    videoDTO.setTagList(tagService.listByLinkId(videoDTO.getId(), ModuleEnum.VIDEO.getCode()));

                    VideoVO videoVO = new VideoVO();
                    BeanUtils.copyProperties(videoDTO, videoVO);
                    if (recommendService.selectRecommendByLinkIdAndType(videoDTO.getId(), ModuleEnum.VIDEO.getCode()) != null) {
                        videoVO.setRecommend(true);
                    } else {
                        videoVO.setRecommend(false);
                    }
                    videoVOList.add(videoVO);
                })));
        Page<VideoVO> videoVOPage = new Page<>();
        BeanUtils.copyProperties(videoDTOPage, videoVOPage);
        videoVOPage.setRecords(videoVOList);
        return new PageUtils(videoVOPage);
    }

    /**
     * 保存视频
     *
     * @param videoVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVideo(VideoVO videoVO) {
        baseMapper.insert(videoVO);
        tagService.saveTagAndNew(videoVO.getTagList(),videoVO.getId(),ModuleEnum.VIDEO.getCode());
        InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
        initGitalkRequest.setId(videoVO.getId());
        initGitalkRequest.setTitle(videoVO.getTitle());
        initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_VIDEO);
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_VIDEO_ADD_ROUTINGKEY, JsonUtils.objectToJson(videoVO));
    }

    /**
     * 更新视频
     *
     * @param videoVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVideo(VideoVO videoVO) {
        // 删除多对多所属标签
        tagService.deleteTagLink(videoVO.getId(),ModuleEnum.VIDEO.getCode());
        // 更新所属标签
        tagService.saveTagAndNew(videoVO.getTagList(),videoVO.getId(), ModuleEnum.VIDEO.getCode());
        // 更新博文
        baseMapper.updateVideoById(videoVO);
        if (videoVO.getRecommend() != null){
            if (videoVO.getRecommend()){
                if (recommendService.selectRecommendByLinkIdAndType(videoVO.getId(), ModuleEnum.VIDEO.getCode()) == null){
                    Integer maxOrderNum = recommendService.selectRecommendMaxOrderNum();
                    Recommend recommend = new Recommend();
                    LocalDateTime now = LocalDateTime.now();
                    recommend.setModule(ModuleEnum.VIDEO.getCode());
                    recommend.setLinkId(videoVO.getId());
                    recommend.setOrderNum(maxOrderNum + 1);
                    recommend.setCreateTime(now);
                    recommend.setUpdateTime(now);
                    recommendService.insertRecommend(recommend);
                }
            }else {
                if (recommendService.selectRecommendByLinkIdAndType(videoVO.getId(), ModuleEnum.VIDEO.getCode()) != null){
                    recommendService.deleteRecommendsByLinkIdsAndType(Arrays.asList(videoVO.getId()), ModuleEnum.VIDEO.getCode());
                }
            }
        }
        // 发送rabbitmq消息同步到es
        InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
        initGitalkRequest.setId(videoVO.getId());
        initGitalkRequest.setTitle(videoVO.getTitle());
        initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_VIDEO);
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_VIDEO_UPDATE_ROUTINGKEY, JsonUtils.objectToJson(videoVO));
    }

    /**
     * 更新视频状态
     *
     * @param videoVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVideoStatus(VideoVO videoVO) {
        if (videoVO.getPublish() != null){
            // 更新发布状态
            baseMapper.updateVideoById(videoVO);
            if (videoVO.getPublish()){
                rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_VIDEO_ADD_ROUTINGKEY, JsonUtils.objectToJson(baseMapper.selectVideoById(videoVO.getId())));
            }else {
                Integer[] ids = {videoVO.getId()};
                rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_VIDEO_DELETE_ROUTINGKEY, JsonUtils.objectToJson(ids));
            }
        }
        if (videoVO.getRecommend() != null){
            // 更新推荐状态
            if (videoVO.getRecommend()){
                if (recommendService.selectRecommendByLinkIdAndType(videoVO.getId(), ModuleEnum.VIDEO.getCode()) == null){
                    Integer maxOrderNum = recommendService.selectRecommendMaxOrderNum();
                    Recommend recommend = new Recommend();
                    LocalDateTime now = LocalDateTime.now();
                    recommend.setModule(ModuleEnum.VIDEO.getCode());
                    recommend.setLinkId(videoVO.getId());
                    recommend.setOrderNum(maxOrderNum + 1);
                    recommend.setCreateTime(now);
                    recommend.setUpdateTime(now);
                    recommendService.insertRecommend(recommend);
                }
            }else {
                if (recommendService.selectRecommendByLinkIdAndType(videoVO.getId(), ModuleEnum.VIDEO.getCode()) != null){
                    recommendService.deleteRecommendsByLinkIdsAndType(Arrays.asList(videoVO.getId()), ModuleEnum.VIDEO.getCode());
                }
            }
        }
    }

    /**
     * 获取视频对象
     *
     * @param videoId
     * @return
     */
    @Override
    public VideoVO getVideo(Integer videoId) {
        VideoVO videoVO = new VideoVO();
        Video video = this.baseMapper.selectById(videoId);
        BeanUtils.copyProperties(video, videoVO);
        // 查询所属标签
        videoVO.setTagList(tagService.listByLinkId(videoId, ModuleEnum.VIDEO.getCode()));
        Recommend recommend = recommendService.selectRecommendByLinkIdAndType(videoId, ModuleEnum.VIDEO.getCode());
        if (recommend != null){
            videoVO.setRecommend(true);
        }else {
            videoVO.setRecommend(false);
        }
        return videoVO;
    }

    /**
     * 判断类别下是否有视频
     * @param categoryId
     * @return
     */
    @Override
    public boolean checkByCategory(Integer categoryId) {
        return baseMapper.checkByCategory(categoryId) > 0;
    }

    /**
     * 判断上传文件下是否有文章
     * @param url
     * @return
     */
    @Override
    public boolean checkByFile(String url) {
        return baseMapper.checkByFile(url) > 0;
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVideos(Integer[] ids) {
        //先删除博文标签多对多关联
        Arrays.stream(ids).forEach(videoId -> {
            tagService.deleteTagLink(videoId, ModuleEnum.VIDEO.getCode());
        });
        this.baseMapper.deleteBatchIds(Arrays.asList(ids));

        recommendService.deleteRecommendsByLinkIdsAndType(Arrays.asList(ids), ModuleEnum.VIDEO.getCode());
        // 发送rabbitmq消息同步到es
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_VIDEO_DELETE_ROUTINGKEY, JsonUtils.objectToJson(ids));
    }

    /********************** portal ********************************/

    /**
     * 分页分类获取列表
     *
     * @param page
     * @param limit
     * @param latest
     * @param categoryId
     * @param like
     * @param watch
     * @return
     */
    @Override
    public PageUtils queryPageCondition(Integer page, Integer limit, Boolean latest, Integer categoryId, Boolean like, Boolean watch) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("latest", latest);
        params.put("like", like);
        params.put("watch", watch);
        if (categoryId != null){
            params.put("categoryId", String.valueOf(categoryId));
        }

        Page<VideoDTO> videoDTOPage = new Query<VideoDTO>(params).getPage();
        List<VideoDTO> videoDTOList = baseMapper.queryPageCondition(videoDTOPage, params);
        if (videoDTOList == null){
            videoDTOList = new ArrayList<>();
        }
        // 封装VideoDTO
        videoDTOPage.setRecords(videoDTOList);
        return new PageUtils(videoDTOPage);
    }

    /**
     * 获取VideoDTO对象
     *
     * @param videoId
     * @return
     */
    @Override
    public VideoDTO getVideoDTOById(Integer videoId) {
        Video video = baseMapper.selectVideoById(videoId);
        if (video == null){
            return null;
        }
        VideoDTO videoDTO = new VideoDTO();
        BeanUtils.copyProperties(video, videoDTO);
        videoDTO.setTagList(tagService.listByLinkId(videoId, ModuleEnum.VIDEO.getCode()));
        // 观看数量
        baseMapper.updateWatchNum(videoId);
        return videoDTO;
    }

    /**
     * 获取热观榜
     * @return
     */
    @Override
    public List<VideoVO> getHotWatchList() {
        List<VideoDTO> videoDTOList = baseMapper.getHotWatchList();
        List<VideoVO> videoVOList = new ArrayList<>();
        videoDTOList.forEach(videoDTOListItem -> {
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(videoDTOListItem, videoVO);
            videoVOList.add(videoVO);
        });
        return videoVOList;
    }

    @Override
    public Boolean likeVideo(Integer id) {
        return baseMapper.updateLikeNum(id);
    }

}
