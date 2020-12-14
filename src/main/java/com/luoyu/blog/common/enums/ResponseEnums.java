package com.luoyu.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorEnum
 *
 * @author luoyu
 * @date 2018/10/07 14:19
 * @description error类型枚举类
 */
@Getter
@AllArgsConstructor
public enum ResponseEnums {

    SUCCESS(200,"成功"),

    // 系统错误
    UNKNOWN(500,"系统内部错误，请联系管理员"),
    PATH_NOT_FOUND(404,"路径不存在，请检查路径"),
    NO_AUTH(403,"没有权限，请联系管理员"),
    DUPLICATE_KEY(501,"数据库中已存在该记录"),
    TOKEN_GENERATOR_ERROR(502,"token生成失败"),
    NO_UUID(503,"uuid为空"),
    SQL_ILLEGAL(504,"sql非法"),

    //用户权限错误
    INVALID_TOKEN(1001,"token不合法"),

    //登录模块错误
    LOGIN_FAIL(10001,"登录失败"),
    CAPTCHA_WRONG(10002,"验证码错误"),
    USERNAME_OR_PASSWORD_WRONG(10003,"用户名或密码错误"),
    ACCOUNT_LOCK(10004,"账号已被锁定，请联系管理员"),

    //七牛OSS错误
    OSS_CONFIG_ERROR(10050,"七牛配置信息错误"),
    OSS_UPLOAD_ERROR(10051,"七牛云OSS上传失败"),

    //Minio错误
    MINIO_UPLOAD_ERROR(10060,"Minio上传失败"),
    MINIO_DOWNLOAD_ERROR(10061,"Minio下载失败"),
    MINIO_GET_URL_ERROR(10062,"Minio获取下载地址失败"),
    MINIO_INIT_ERROR(10063,"Minio初始化失败"),
    MINIO_DELETE_FILE_ERROR(10064,"Minio删除文件失败"),
    MINIO_GET_FILE_ERROR(10065,"Minio获取文件失败"),
    MINIO_BUCKET_EXISTS_ERROR(10066,"Minio检查存储桶是否存在失败"),
    MINIO_CREATE_BUCKET_ERROR(10067,"Minio创建存储桶失败"),
    MINIO_GET_BUCKET_ERROR(10068,"Minio获取存储桶失败"),
    MINIO_DELETE_BUCKET_ERROR(10069,"Minio删除存储桶失败"),

    //Zxing错误
    ZXING_CREATE_ERROR(10070,"生成二维码失败"),

    //参数
    PARAM_ERROR(20001,"参数错误"),
    SAVE_FAIL(20002,"保存数据失败"),
    UPDATE_FAILR(20003,"更新数据失败"),
    DELETE_FAIL(20004,"删除数据失败");

    private int code;
    private String msg;

}
