package com.luoyu.blog.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.file.FileResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 云存储资源表 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
public interface FileResourceMapper extends BaseMapper<FileResource> {

    /**
     * 根据文件md5，模块查询是否上传过
     *
     * @param fileMd5 fileMd5
     * @param module module
     * @return
     */
    FileResource selectFileResourceByFileMd5AndModule(@Param("fileMd5") String fileMd5, @Param("module") Integer module);

    /**
     * 根据文件md5，模块更新状态
     *
     * @param fileResource fileResource
     * @return
     */
    Boolean updateFileResourceByFileMd5AndModule(FileResource fileResource);

    /**
     * 批量查询文件
     *
     * @param ids ids
     * @return
     */
    List<FileResource> selectFileResourceByIds(@Param("ids") Integer[] ids);

}
