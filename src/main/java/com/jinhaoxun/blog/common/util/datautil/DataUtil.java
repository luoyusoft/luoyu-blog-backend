package com.jinhaoxun.blog.common.com.util.datautil;

import com.alibaba.fastjson.JSONObject;
import com.jinhaoxun.blog.project.pojo.shiro.UserContext;
import net.sf.json.JSONArray;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Data工具类
 */
public class DataUtil {

    /**
     * @author jinhaoxun
     * @description 获取当前登录用户id
     * @return String 当前登录用户id
     */
    public static Long getUserId(){
        return UserContext.getCurrentUser().getUserId();
    }

    /**
     * @author jinhaoxun
     * @description 获取当前登录用户姓名
     * @return String 当前登录用户姓名
     */
    public static String getName(){
        return UserContext.getCurrentUser().getName();
    }

    /**
     * @author jinhaoxun
     * @description 实体对象转成Map方法
     * @param obj 实体对象
     * @return Map 实体对象转换后的Map
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @author jinhaoxun
     * @description Map转成实体对象方法
     * @param map map实体对象包含属性
     * @param clazz 实体对象类型
     * @return Object Map数据转换后的对象
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @author jinhaoxun
     * @description Json转List方法
     * @param s json数据
     * @return List JSON数据转换后的List
     */
    public static List<String> jsonToList(String s){
        List<String> list =new ArrayList<>();
        try {
            JSONArray jsonArray = JSONArray.fromObject(s);
            for (int i=0;i<jsonArray.size();i++){
                list.add((String) jsonArray.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @author jinhaoxun
     * @description JSON格式字符串转换为Map方法
     * @param json JSON字符串
     * @return Map JSON数据转换后的Map
     */
    public static Map<String, String> jsonToMap(JSONObject json){
        Map<String, String> map = new HashMap<>();
        Set<String> jsonSet = json.keySet();
        for (String s : jsonSet) {
            String value = json.getString(s);
            map.put(s, value);
        }
        return map;
    }
}
