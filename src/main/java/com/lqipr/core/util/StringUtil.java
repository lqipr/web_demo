package com.lqipr.core.util;

/**
 * Created by lqipr on 2015/9/18.
 */
public class StringUtil {

    public static int convert(String str, int defaultValue){
        if(str == null) return defaultValue;
        String s = str.trim();
        if(s.isEmpty()||s.equalsIgnoreCase("undefined")||s.equalsIgnoreCase("null")) return defaultValue;
        try {
            return Integer.parseInt(s);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

}
