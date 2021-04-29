package com.jinhx.blog.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MybatisPlusAutoFillHandler
 *
 * @author luoyu
 * @date 2019/11/10 13:54
 * @description 公共字段自动填充类
 */
@Component
public class MybatisPlusAutoFillHandler implements MetaObjectHandler {

    /**
     * 插入时填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充创建时间 更新时间字段
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("createTime", now, metaObject);
        this.setFieldValByName("updateTime", now, metaObject);
    }

    /**
     * 更新时填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充给更新时间字段
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

}
