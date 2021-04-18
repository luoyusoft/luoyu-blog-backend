package com.luoyu.blog.controller.log;

import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.article.vo.HomeArticleInfoVO;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.log.vo.HomeLogInfoVO;
import com.luoyu.blog.service.log.LogViewService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TimeLineController
 *
 * @author luoyu
 * @date 2019/02/24 20:46
 * @description
 */
@RestController
public class LogViewController {

    @Autowired
    private LogViewService logViewService;

    /**
     * 获取首页信息
     */
    @GetMapping("/manage/log/homeinfo")
    public Response getHommeLogInfoVO() {
        HomeLogInfoVO hommeLogInfoVO = logViewService.getHommeLogInfoVO();
        return Response.success(hommeLogInfoVO);
    }

    /**
     * 获取列表
     */
    @GetMapping("/manage/log/list")
    @RequiresPermissions("log:list")
    public Response listTimeline(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("module") Integer module) {
        PageUtils logViewPage = logViewService.queryPage(page, limit, module);
        return Response.success(logViewPage);
    }

}
