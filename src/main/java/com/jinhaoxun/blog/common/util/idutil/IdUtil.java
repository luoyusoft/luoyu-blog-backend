package com.jinhaoxun.blog.common.com.util.idutil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Id工具类
 */
public class IdUtil {

    private static long tmpID = 0;

    private static boolean tmpIDlocked = false;

    /**
     * @author jinhaoxun
     * @description 获取long型唯一ID方法
     * @return Boolean
     */
    public static long getId()
    {
        long ltime = 0;
        while (true) {
            if(!tmpIDlocked) {
                tmpIDlocked = true;
                //当前：（年、月、日、时、分、秒、毫秒）*10000
                ltime = Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(new Date())) * 10000;
                if(tmpID < ltime)
                {
                    tmpID = ltime;
                } else {
                    tmpID = tmpID + 1;
                    ltime = tmpID;
                }
                tmpIDlocked = false;
                return ltime;
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 获取UUID唯一ID方法
     * @param machineId 最大支持1-9个集群机器部署
     * @return Boolean
     */
    public static String getOrderIdByUuId(int machineId) {
        int hashCode = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if(hashCode < 0) {
            hashCode = - hashCode;
        }
        //0 代表前面补充0，4 代表长度为4，d 代表参数为正数型
        return machineId + String.format("%015d", hashCode);
    }
}