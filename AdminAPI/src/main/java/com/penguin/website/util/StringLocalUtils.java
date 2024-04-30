package com.penguin.website.util;

/**
 * create by: mlshao
 * description: 本地处理字符串工具
 */
public class StringLocalUtils {
    /**
     * create by: mlshao
     * description: 去除空格
     */
    public static String replaceSpace(String temp) {
        if (temp != null && temp.length() != 0) {
            return temp.replace(" ","");
        }
        return temp;
    }
}
