package com.xbaimiao.minecraft.exppond;

/**
 * @author xbaimiao
 * @date 2022/1/25 9:57 AM
 * @email 3104026189@qq.com
 */
public class Utils {

    /**
     * 传入的a参数是否在b-c范围内
     */
    public static boolean in(int a, int b, int c) {
        int max = Math.max(b, c);
        int min = Math.min(b, c);
        return a <= max && a >= min;
    }

}
