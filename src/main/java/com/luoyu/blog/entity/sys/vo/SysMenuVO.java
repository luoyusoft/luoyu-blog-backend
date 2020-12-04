package com.luoyu.blog.entity.sys.vo;

import com.luoyu.blog.entity.sys.SysMenu;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author luoyu
 * @since 2018-10-19
 */
@Data
public class SysMenuVO implements Serializable {

    List<SysMenu> menuList;
    Set<String> permissions;

}
