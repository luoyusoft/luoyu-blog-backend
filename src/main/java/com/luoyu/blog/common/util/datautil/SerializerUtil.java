package com.luoyu.blog.common.util.datautil;

import com.alibaba.fastjson.JSON;
import com.luoyu.blog.common.exception.CustomRuntimeException;
import com.luoyu.blog.common.response.ResponseMsg;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Serializable工具(JDK)(也可以使用Protobuf自行百度)
 */
public class SerializerUtil {

    /**
     * 自定义UTF_8
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * @author jinhaoxun
     * @description 将对象序列化方法
     * @param obj 进行序列化的实体类
     * @return byte[] 序列化后的数据
     */
    public static <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes(Charset.forName(UTF_8));
    }

    /**
     * @author jinhaoxun
     * @description 将序列化后的数据进行反序列化方法
     * @param data 进行序列化的数据
     * @param clazz 进行序列化的数据的实体类
     * @return T 反序列化后的实体类对象
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    /**
     * @author jinhaoxun
     * @description 将对象序列化方法
     * @param object 进行序列化的对象
     * @return byte[] 序列化后的数据
     * @throws CustomRuntimeException
     */
    public static byte[] serializable(Object object) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomRuntimeException(ResponseMsg.SERIALIZABLE_UTIL_WRONG.getCode(),"系统异常，请稍后重试！","SerializableUtil工具类序列化出现IOException异常");
        } finally {
            try {
                if(oos != null) {
                    oos.close();
                }
                if(baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @author jinhaoxun
     * @description 将序列化后的数据进行反序列化方法
     * @param bytes 进行序列化后的数据
     * @return Object 反序列化后的对象
     * @throws CustomRuntimeException
     */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new CustomRuntimeException(ResponseMsg.SERIALIZABLE_UTIL_WRONG.getCode(),"系统异常，请稍后重试！","SerializableUtil工具类反序列化出现ClassNotFoundException异常");
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomRuntimeException(ResponseMsg.SERIALIZABLE_UTIL_WRONG.getCode(),"系统异常，请稍后重试！","SerializableUtil工具类反序列化出现IOException异常");
        } finally {
            try {
                if(ois != null) {
                    ois.close();
                }
                if(bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}