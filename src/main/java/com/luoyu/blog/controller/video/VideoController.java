package com.luoyu.blog.controller.video;

import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.entity.video.vo.VideoVO;
import com.luoyu.blog.service.video.VideoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * VideoController
 *
 * @author luoyu
 * @date 2018/11/20 20:25
 * @description
 */
@RestController
@CacheConfig(cacheNames ={RedisCacheNames.VIDEO})
public class VideoController {

    @Resource
    private VideoService videoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 列表
     */
    @GetMapping("/manage/video/list")
    @RequiresPermissions("video:list")
    public Response listVideo(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title) {
        PageUtils videoPage = videoService.queryPage(page, limit, title);
        return Response.success(videoPage);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/video/info/{videoId}")
    @RequiresPermissions("video:list")
    public Response info(@PathVariable("videoId") Integer videoId) {
        VideoVO videoVO = videoService.getVideo(videoId);
        return Response.success(videoVO);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/video/save")
    @RequiresPermissions("video:save")
    @CacheEvict(allEntries = true)
    public Response saveVideo(@RequestBody VideoVO videoVO){
        ValidatorUtils.validateEntity(videoVO, AddGroup.class);
        videoService.saveVideo(videoVO);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/video/update")
    @RequiresPermissions("video:update")
    @CacheEvict(allEntries = true)
    public Response updateVideo(@RequestBody VideoVO videoVO){
        videoService.updateVideo(videoVO);
        return Response.success();
    }

    /**
     * 修改状态
     */
    @PutMapping("/manage/video/update/status")
    @RequiresPermissions("video:update")
    @CacheEvict(allEntries = true)
    public Response updateVideoStatus(@RequestBody VideoVO videoVO){
        videoService.updateVideoStatus(videoVO);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/video/delete")
    @RequiresPermissions("video:delete")
    @CacheEvict(allEntries = true)
    public Response deleteVideos(@RequestBody Integer[] ids) {
        videoService.deleteVideos(ids);
        return Response.success();
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/manage/video/cache/refresh")
    @RequiresPermissions("video:cache:refresh")
    public Response flush() {
        Set<String> keys = redisTemplate.keys(RedisCacheNames.PROFIX + "*");
        redisTemplate.delete(keys);

        return Response.success();
    }

    /********************** portal ********************************/

    @GetMapping("/video/{videoId}")
    @LogView(module = 1)
    public Response getVideo(@PathVariable Integer videoId){
        VideoDTO videoDTO = videoService.getVideoDTOById(videoId);
        return Response.success(videoDTO);
    }

    @PutMapping("/video/like/{id}")
    @LogView(module = 1)
    public Response likeVideo(@PathVariable Integer id) {
        return Response.success(videoService.likeVideo(id));
    }

    @GetMapping("/videos")
    @Cacheable
    @LogView(module = 1)
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                         @RequestParam("latest") Boolean latest, @RequestParam("categoryId") Integer categoryId,
                         @RequestParam("like") Boolean like, @RequestParam("watch") Boolean watch) {
        PageUtils queryPageCondition = videoService.queryPageCondition(page, limit, latest, categoryId, like, watch);
        return Response.success(queryPageCondition);
    }

    @GetMapping("/videos/hotwatch")
    @Cacheable
    @LogView(module = 1)
    public Response getHotWatchList(){
        return Response.success(videoService.getHotWatchList());
    }

}
