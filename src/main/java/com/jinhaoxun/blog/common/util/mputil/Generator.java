package com.jinhaoxun.blog.common.com.util.mputil;

import java.util.Scanner;

/**
 * @version: 1.0
 * @author jinhaoxun
 * @Date 2018-05-09
 * @description MybatisPlus逆向工程
 */
public class Generator {

    /**
     * 自定义UTF_8
     */
    private static final String GENERATOR_TYPE_1 = "1";

    /**
     * @author jinhaoxun
     * @description 读取控制台的内容
     * @return int 读取到的数据
     */
    public static int scanner() {
        Scanner scanner = new Scanner(System.in);
        String help = " ！！代码生成, 输入 0 表示使用 Velocity 引擎 ！！" +
                "\n对照表：" +
                "\n0 = Velocity 引擎" +
                "\n1 = Freemarker 引擎" +
                "\n请输入：";
        System.out.println(help);
        int slt = 0;
        // 现在有输入数据
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (GENERATOR_TYPE_1.equals(ipt)) {
                slt = 1;
            }
        }
        return slt;
    }
}

