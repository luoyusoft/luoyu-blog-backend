package com.luoyu.blogmanage.entity.sys.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.luoyu.blogmanage.entity.sys.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
