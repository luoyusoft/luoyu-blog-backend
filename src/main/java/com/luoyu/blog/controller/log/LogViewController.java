package com.luoyu.blog.controller.log;

import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.service.log.LogViewService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TimeLineController
 *
 * @author luoyu
 * @date 2019/02/24 20:46
 * @description
 */
@RestController
public class LogViewController {

    @Resource
    private LogViewService logViewService;

    @GetMapping("/manage/log/list")
    @RequiresPermissions("log:list")
    public Response listTimeline(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("type") Integer type) {
        PageUtils logViewPage = logViewService.queryPage(page, limit, type);
        return Response.success(logViewPage);
    }

}
