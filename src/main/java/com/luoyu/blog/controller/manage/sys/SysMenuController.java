package com.luoyu.blog.controller.manage.sys;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.entity.sys.SysMenu;
import com.luoyu.blog.common.enums.MenuTypeEnum;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.entity.sys.vo.SysMenuVO;
import com.luoyu.blog.service.sys.SysMenuService;
import com.luoyu.blog.service.sys.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-10-19
 */

@RestController
@RequestMapping("/admin/sys/menu")
public class SysMenuController extends AbstractController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private ShiroService shiroService;

    /**
     * 信息
     */
    @GetMapping("/nav")
    public Response nav(){
        List<SysMenu> menuList=sysMenuService.listUserMenu(getUserId());
        Set<String> permissions=shiroService.getUserPermissions(getUserId());
        SysMenuVO sysMenuVO = new SysMenuVO();
        sysMenuVO.setMenuList(menuList);
        sysMenuVO.setPermissions(permissions);
        return Response.success(sysMenuVO);
    }

    /**
     * 所有菜单列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public Response list(){
        List<SysMenu> menuList = sysMenuService.list(null);
        menuList.forEach(sysMenu -> {
            SysMenu parentMenu = sysMenuService.getById(sysMenu.getParentId());
            if(parentMenu != null){
                sysMenu.setParentName(parentMenu.getName());
            }
        });
        return Response.success(menuList);
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public Response select(){
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.queryNotButtonList();

        //添加顶级菜单
        SysMenu root = new SysMenu();
        root.setId(0);
        root.setName("一级菜单");
        root.setParentId(-1);
        root.setOpen(true);
        menuList.add(root);

        return Response.success(menuList);
    }

    /**
     * 获取单个菜单信息
     * @param menuId
     * @return
     */
    @GetMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public Response update(@PathVariable("menuId") Integer menuId){
        SysMenu menu = sysMenuService.getById(menuId);
        return Response.success(menu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public Response save(@RequestBody SysMenu menu){
        //数据校验
        verifyForm(menu);
        sysMenuService.save(menu);

        return Response.success();
    }

    /**
     * 更新
     * @param menu
     * @return
     */
    @PutMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public Response update(@RequestBody SysMenu menu){
        //数据校验
        verifyForm(menu);
        sysMenuService.updateById(menu);

        return Response.success();
    }

    /**
     * 删除
     * @param menuId
     * @return
     */
    @DeleteMapping("/delete/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    public Response delete(@PathVariable("menuId") Integer menuId){
        if(menuId <= 29){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "系统菜单，不能删除");
        }

        //判断是否有子菜单或按钮
        List<SysMenu> menuList = sysMenuService.queryListParentId(menuId);
        if(menuList.size() > 0){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "请先删除子菜单或按钮");
        }
        sysMenuService.delete(menuId);
        return Response.success();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == MenuTypeEnum.MENU.getCode()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = MenuTypeEnum.CATALOG.getCode();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == MenuTypeEnum.CATALOG.getCode() ||
                menu.getType() == MenuTypeEnum.MENU.getCode()) {
            if (parentType != MenuTypeEnum.CATALOG.getCode()) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上级菜单只能为目录类型");
            }
        }

        //按钮
        if (menu.getType() == MenuTypeEnum.BUTTON.getCode()) {
            if (parentType != MenuTypeEnum.MENU.getCode()) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "上级菜单只能为菜单类型");
            }
        }
    }

}
